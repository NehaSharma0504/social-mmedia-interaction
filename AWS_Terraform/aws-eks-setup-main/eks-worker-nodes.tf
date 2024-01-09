#
# EKS Worker Nodes Resources
#  * IAM role allowing Kubernetes actions to access other AWS services
#  * EKS Node Group to launch worker nodes
#
locals {
  subnettags = flatten([
    for tagkey, tagvalue in var.alb_private_subnets_tags : [
	  for subnetid in var.alb_private_subnets : { 
	    id = subnetid
	    tagkey = tagkey
	    tagvalue = tagvalue 
	  }
	]
  ])
}

resource "aws_iam_role" "K8S-EKS-node" {
  name = "${var.cluster-name}-eks-node-role"

  assume_role_policy = <<POLICY
{
  "Version": "2012-10-17",
  "Statement": [
    {
      "Effect": "Allow",
      "Principal": {
        "Service": "ec2.amazonaws.com"
      },
      "Action": "sts:AssumeRole"
    }
  ]
}
POLICY
}

resource "aws_iam_role_policy_attachment" "K8S-EKS-node-AmazonEKSWorkerNodePolicy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKSWorkerNodePolicy"
  role       = aws_iam_role.K8S-EKS-node.name
}

resource "aws_iam_role_policy_attachment" "K8S-EKS-node-AmazonEKS_CNI_Policy" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEKS_CNI_Policy"
  role       = aws_iam_role.K8S-EKS-node.name
}

resource "aws_iam_role_policy_attachment" "K8S-EKS-node-AmazonEC2ContainerRegistryReadOnly" {
  policy_arn = "arn:aws:iam::aws:policy/AmazonEC2ContainerRegistryReadOnly"
  role       = aws_iam_role.K8S-EKS-node.name
}

resource "aws_eks_node_group" "K8S-EKS" {
  cluster_name    = aws_eks_cluster.K8S-EKS.name
  node_group_name = "${var.cluster-name}-NODEGROUP"
  node_role_arn   = aws_iam_role.K8S-EKS-node.arn
  subnet_ids      = "${var.subnet-ids}"
  instance_types  = ["t3.xlarge"]
  #instance_id     = "ami-01979f2a9bb954e6c"
  disk_size       = 50
  scaling_config {
    desired_size = 1
    max_size     = 1
    min_size     = 1
  }
  
  remote_access {
    #source_security_group_ids = [""]
    ec2_ssh_key   = "aws_key_pair"
  } 
 tags = tomap({
    "Name" = "${var.cluster-name}",
    "kubernetes.io/cluster/${var.cluster-name}" = "shared"
  })

  depends_on = [
    aws_iam_role_policy_attachment.K8S-EKS-node-AmazonEKSWorkerNodePolicy,
    aws_iam_role_policy_attachment.K8S-EKS-node-AmazonEKS_CNI_Policy,
    aws_iam_role_policy_attachment.K8S-EKS-node-AmazonEC2ContainerRegistryReadOnly,
  ]
}

resource "aws_ec2_tag" "private_subnet_tag" {
  for_each = {for sub in local.subnettags : "${sub.id}-${sub.tagkey}-${sub.tagvalue}" => sub}
  #for_each = {for sub in local.subnettags}
  resource_id = "${each.value.id}"
  key         = "${each.value.tagkey}"
  value       = "${each.value.tagvalue}"
}

# resource "kubernetes_namespace" "kube-namespace" {
#   metadata {
#     annotations = {
#       name = var.namespace
#     }
#     name = var.namespace
#   }
# depends_on = [aws_eks_node_group.K8S-EKS]
# }

