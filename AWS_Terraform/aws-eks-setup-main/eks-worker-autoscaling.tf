#resource "helm_release" "metrics-server" {
#
#  name       = "metrics-server"
#  repository = "https://kubernetes-sigs.github.io/metrics-server/"
#  chart      = "metrics-server"
#  version    = "3.5.0"
#  namespace  = var.k8s_namespace
#  atomic     = true
# timeout    = 900
#  set {
#    name  = "replicaCount"
#    value = 1
#  }
#  depends_on = [aws_eks_node_group.K8S-EKS, aws_eks_cluster.K8S-EKS]
#}

resource "aws_iam_role_policy_attachment" "workers_autoscaling" {
  policy_arn = aws_iam_policy.worker_autoscaling.arn
  role       = "${var.cluster-name}-eks-node-role"
  depends_on = [aws_eks_node_group.K8S-EKS, aws_eks_cluster.K8S-EKS]
}

resource "aws_iam_policy" "worker_autoscaling" {
  name_prefix = "${var.cluster-name}-eks-worker-autoscaling-policy"
  description = "EKS worker node autoscaling policy for cluster"
  policy      = data.aws_iam_policy_document.worker_autoscaling.json
}

data "aws_iam_policy_document" "worker_autoscaling" {
  statement {
    sid    = "eksWorkerAutoscalingAll"
    effect = "Allow"

    actions = [
      "autoscaling:DescribeAutoScalingGroups",
      "autoscaling:DescribeAutoScalingInstances",
      "autoscaling:DescribeLaunchConfigurations",
      "autoscaling:DescribeTags",
      "ec2:DescribeLaunchTemplateVersions",
    ]

    resources = ["*"]
  }

  statement {
    sid    = "eksWorkerAutoscalingOwn"
    effect = "Allow"

    actions = [
      "autoscaling:SetDesiredCapacity",
      "autoscaling:TerminateInstanceInAutoScalingGroup",
      "autoscaling:UpdateAutoScalingGroup",
    ]

    resources = ["*"]

    }
  }
# Generate a kubeconfig file for the EKS cluster to use in provisioners
data "template_file" "kubeconfig" {
 depends_on = [aws_eks_cluster.K8S-EKS]
  template = <<-EOF
    apiVersion: v1
    kind: Config
    current-context: terraform
    clusters:
    - name: ${data.aws_eks_cluster.clustername.name}
      cluster:
        certificate-authority-data: ${data.aws_eks_cluster.clustername.certificate_authority.0.data}
        server: ${data.aws_eks_cluster.clustername.endpoint}
    contexts:
    - name: terraform
      context:
        cluster: ${data.aws_eks_cluster.clustername.name}
        user: terraform
    users:
    - name: terraform
      user:
        token: ${data.aws_eks_cluster_auth.cluster-auth.token}
  EOF

}

resource "null_resource" "cluster-autoscaler-deploy" {

 triggers = {
    kubeconfig = base64encode(data.template_file.kubeconfig.rendered)
    cmd_create = "kubectl-apply"
  }

# provisioner "local-exec" {
#   when    = create
#   command = <<EOT
#   type cluster-autoscaler-autodiscover.yaml | kubectl -n ${var.k8s_namespace} --kubeconfig <(echo $KUBECONFIG | base64 --decode )apply -f -
#   EOT
# }
provisioner "local-exec" {
  command = "echo '${data.template_file.kubeconfig.rendered}' > ~/.kube/config"
}
    
depends_on = [aws_iam_role_policy_attachment.workers_autoscaling , aws_iam_policy.worker_autoscaling, aws_eks_node_group.K8S-EKS, aws_eks_cluster.K8S-EKS]
}
