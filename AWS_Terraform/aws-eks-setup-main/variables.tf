variable "aws_region" {
  default = "us-east-1"
}

variable "aws_profile" {
  default = ""
}
variable "cluster-name" {
  default = "EKS-CLUSTER"
  type    = string
}

variable "vpc_id" {
  default = "vpc-002943b43a19be740"
}

variable "subnet-ids" {
  default = ["subnet-02ba3231677732578", "subnet-07bc2a28433536450"]
  type    = list(string)
}

variable "alb_private_subnets" {
  type    = list(string)
  default = ["subnet-009ce01f881fb2cac","subnet-066d1e07055b73684"]
}
variable "alb_private_subnets_tags" {
  type    = map
  default = {
    "kubernetes.io/role/elb" = 1
    "kubernetes.io/cluster/K8S-GLB-INTEGRATION-V3-EKS-CLUSTER" = "shared"
  }
}
variable "namespace" {
   type = string
   default = "kubernetes-namespace"
}
variable "k8s_namespace" {
  description = "Kubernetes namespace to deploy the AWS Load Balancer Controller into."
  type        = string
  default     = "kube-system"
}
