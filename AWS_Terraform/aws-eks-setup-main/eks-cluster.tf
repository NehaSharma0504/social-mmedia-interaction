#
# EKS Cluster Resources
#  * IAM Role to allow EKS service to manage other AWS services
#  * EC2 Security Group to allow networking traffic with EKS cluster
#  * EKS Cluster
#

resource "aws_iam_role" "K8S-EKS-cluster" {
  name = "${var.cluster-name}-Cluster-Role"

  assume_role_policy = <<POLICY
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "eks.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
POLICY
}

resource "aws_iam_role_policy_attachment" "K8S-EKS-cluster-AmazonEKSClusterPolicy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSClusterPolicy"
  role       = aws_iam_role.K8S-EKS-cluster.name
}

resource "aws_iam_role_policy_attachment" "K8S-EKS-cluster-AmazonEKSVPCResourceController" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSVPCResourceController"
  role       = aws_iam_role.K8S-EKS-cluster.name
}


resource "aws_eks_cluster" "K8S-EKS" {
  name     = var.cluster-name
  version  = "1.26"
  role_arn = aws_iam_role.K8S-EKS-cluster.arn
  vpc_config {
    subnet_ids         = "${var.subnet-ids}"
    endpoint_private_access = "true"
    endpoint_public_access = "false"
  }
  enabled_cluster_log_types = ["api", "audit", "authenticator", "controllerManager", "scheduler"]
  depends_on = [
    aws_cloudwatch_log_group.K8S-EKS,
    aws_iam_role_policy_attachment.K8S-EKS-cluster-AmazonEKSClusterPolicy,
    aws_iam_role_policy_attachment.K8S-EKS-cluster-AmazonEKSVPCResourceController,
  ]
}

data "tls_certificate" "k8s-tls-cert" {
   url = aws_eks_cluster.K8S-EKS.identity[0].oidc[0].issuer
   depends_on=[aws_eks_cluster.K8S-EKS]
}


resource "aws_iam_openid_connect_provider" "openid-connect" {
   client_id_list = ["sts.amazonaws.com"]
   thumbprint_list = [data.tls_certificate.k8s-tls-cert.certificates[0].sha1_fingerprint]
   url = aws_eks_cluster.K8S-EKS.identity[0].oidc[0].issuer
}

resource "aws_cloudwatch_log_group" "K8S-EKS" {
  name = "/aws/eks/${var.cluster-name}/cluster"
  retention_in_days = 7
}

output "endpoint" {
  value = aws_eks_cluster.K8S-EKS.endpoint
}

output "kubeconfig-certificate-authority-data" {
  value = aws_eks_cluster.K8S-EKS.certificate_authority[0].data
}
