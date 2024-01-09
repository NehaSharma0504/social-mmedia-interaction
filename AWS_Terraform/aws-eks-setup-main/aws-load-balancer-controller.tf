data "aws_caller_identity" "current" {}

data "aws_eks_cluster_auth" "cluster-auth" {
  depends_on = [aws_eks_cluster.K8S-EKS]
  name       = aws_eks_cluster.K8S-EKS.name
}

data "aws_eks_cluster" "clustername" {
  name       = var.cluster-name
depends_on =[aws_eks_node_group.K8S-EKS, 
            aws_eks_cluster.K8S-EKS]
}
data "aws_iam_policy_document" "eks_oidc_assume_role" {
  count = 1
  statement {
    actions = ["sts:AssumeRoleWithWebIdentity"]
    effect  = "Allow"
    condition {
      test     = "StringEquals"
      variable = "${replace(data.aws_eks_cluster.clustername.identity[0].oidc[0].issuer, "https://", "")}:sub"
      values = [
        "system:serviceaccount:${var.k8s_namespace}:aws-load-balancer-controller"
      ]
    }
    principals {
      identifiers = [
        "arn:aws:iam::${data.aws_caller_identity.current.account_id}:oidc-provider/${replace(data.aws_eks_cluster.clustername.identity[0].oidc[0].issuer, "https://", "")}"
      ]
      type = "Federated"
    }
  }
}

provider "kubernetes" {
 # alias = "eks"
  host                   = data.aws_eks_cluster.clustername.endpoint
  token                  = data.aws_eks_cluster_auth.cluster-auth.token
  cluster_ca_certificate = base64decode(data.aws_eks_cluster.clustername.certificate_authority[0].data)
}

resource "aws_iam_policy" "aws-alb-management-policy" {
  name        = "${var.cluster-name}-alb-management"
  description = "Permissions that are required to manage AWS Application Load Balancers."
  # Source: `curl -o iam-policy.json https://raw.githubusercontent.com/kubernetes-sigs/aws-load-balancer-controller/v2.2.1/docs/install/iam_policy.json`
  policy = <<-POLICY
{
    "Version": "2012-10-17",
    "Statement": [
        {
            "Effect": "Allow",
            "Action": [
                "iam:CreateServiceLinkedRole",
                "ec2:DescribeAccountAttributes",
                "ec2:DescribeAddresses",
                "ec2:DescribeAvailabilityZones",
                "ec2:DescribeInternetGateways",
                "ec2:DescribeVpcs",
                "ec2:DescribeSubnets",
                "ec2:DescribeSecurityGroups",
                "ec2:DescribeInstances",
                "ec2:DescribeNetworkInterfaces",
                "ec2:DescribeTags",
                "ec2:GetCoipPoolUsage",
                "ec2:DescribeCoipPools",
                "elasticloadbalancing:DescribeLoadBalancers",
                "elasticloadbalancing:DescribeLoadBalancerAttributes",
                "elasticloadbalancing:DescribeListeners",
                "elasticloadbalancing:DescribeListenerCertificates",
                "elasticloadbalancing:DescribeSSLPolicies",
                "elasticloadbalancing:DescribeRules",
                "elasticloadbalancing:DescribeTargetGroups",
                "elasticloadbalancing:DescribeTargetGroupAttributes",
                "elasticloadbalancing:DescribeTargetHealth",
                "elasticloadbalancing:DescribeTags"
            ],
            "Resource": "*"
        },
        {
            "Effect": "Allow",
            "Action": [
                "cognito-idp:DescribeUserPoolClient",
                "acm:ListCertificates",
                "acm:DescribeCertificate",
                "iam:ListServerCertificates",
                "iam:GetServerCertificate",
                "waf-regional:GetWebACL",
                "waf-regional:GetWebACLForResource",
                "waf-regional:AssociateWebACL",
                "waf-regional:DisassociateWebACL",
                "wafv2:GetWebACL",
                "wafv2:GetWebACLForResource",
                "wafv2:AssociateWebACL",
                "wafv2:DisassociateWebACL",
                "shield:GetSubscriptionState",
                "shield:DescribeProtection",
                "shield:CreateProtection",
                "shield:DeleteProtection"
            ],
            "Resource": "*"
        },
        {
            "Effect": "Allow",
            "Action": [
                "ec2:AuthorizeSecurityGroupIngress",
                "ec2:RevokeSecurityGroupIngress"
            ],
            "Resource": "*"
        },
        {
            "Effect": "Allow",
            "Action": [
                "ec2:CreateSecurityGroup"
            ],
            "Resource": "*"
        },
        {
            "Effect": "Allow",
            "Action": [
                "ec2:CreateTags"
            ],
            "Resource": "arn:aws:ec2:*:*:security-group/*",
            "Condition": {
                "StringEquals": {
                    "ec2:CreateAction": "CreateSecurityGroup"
                },
                "Null": {
                    "aws:RequestTag/elbv2.k8s.aws/cluster": "false"
                }
            }
        },
        {
            "Effect": "Allow",
            "Action": [
                "ec2:CreateTags",
                "ec2:DeleteTags"
            ],
            "Resource": "arn:aws:ec2:*:*:security-group/*",
            "Condition": {
                "Null": {
                    "aws:RequestTag/elbv2.k8s.aws/cluster": "true",
                    "aws:ResourceTag/elbv2.k8s.aws/cluster": "false"
                }
            }
        },
        {
            "Effect": "Allow",
            "Action": [
                "ec2:AuthorizeSecurityGroupIngress",
                "ec2:RevokeSecurityGroupIngress",
                "ec2:DeleteSecurityGroup"
            ],
            "Resource": "*",
            "Condition": {
                "Null": {
                    "aws:ResourceTag/elbv2.k8s.aws/cluster": "false"
                }
            }
        },
        {
            "Effect": "Allow",
            "Action": [
                "elasticloadbalancing:CreateLoadBalancer",
                "elasticloadbalancing:CreateTargetGroup"
            ],
            "Resource": "*",
            "Condition": {
                "Null": {
                    "aws:RequestTag/elbv2.k8s.aws/cluster": "false"
                }
            }
        },
        {
            "Effect": "Allow",
            "Action": [
                "elasticloadbalancing:CreateListener",
                "elasticloadbalancing:DeleteListener",
                "elasticloadbalancing:CreateRule",
                "elasticloadbalancing:DeleteRule"
            ],
            "Resource": "*"
        },
        {
            "Effect": "Allow",
            "Action": [
                "elasticloadbalancing:AddTags",
                "elasticloadbalancing:RemoveTags"
            ],
            "Resource": [
                "arn:aws:elasticloadbalancing:*:*:targetgroup/*/*",
                "arn:aws:elasticloadbalancing:*:*:loadbalancer/net/*/*",
                "arn:aws:elasticloadbalancing:*:*:loadbalancer/app/*/*"
            ],
            "Condition": {
                "Null": {
                    "aws:RequestTag/elbv2.k8s.aws/cluster": "true",
                    "aws:ResourceTag/elbv2.k8s.aws/cluster": "false"
                }
            }
        },
        {
            "Effect": "Allow",
            "Action": [
                "elasticloadbalancing:AddTags",
                "elasticloadbalancing:RemoveTags"
            ],
            "Resource": [
                "arn:aws:elasticloadbalancing:*:*:listener/net/*/*/*",
                "arn:aws:elasticloadbalancing:*:*:listener/app/*/*/*",
                "arn:aws:elasticloadbalancing:*:*:listener-rule/net/*/*/*",
                "arn:aws:elasticloadbalancing:*:*:listener-rule/app/*/*/*"
            ]
        },
        {
            "Effect": "Allow",
            "Action": [
                "elasticloadbalancing:ModifyLoadBalancerAttributes",
                "elasticloadbalancing:SetIpAddressType",
                "elasticloadbalancing:SetSecurityGroups",
                "elasticloadbalancing:SetSubnets",
                "elasticloadbalancing:DeleteLoadBalancer",
                "elasticloadbalancing:ModifyTargetGroup",
                "elasticloadbalancing:ModifyTargetGroupAttributes",
                "elasticloadbalancing:DeleteTargetGroup"
            ],
            "Resource": "*",
            "Condition": {
                "Null": {
                    "aws:ResourceTag/elbv2.k8s.aws/cluster": "false"
                }
            }
        },
        {
            "Effect": "Allow",
            "Action": [
                "elasticloadbalancing:RegisterTargets",
                "elasticloadbalancing:DeregisterTargets"
            ],
            "Resource": "arn:aws:elasticloadbalancing:*:*:targetgroup/*/*"
        },
        {
            "Effect": "Allow",
            "Action": [
                "elasticloadbalancing:SetWebAcl",
                "elasticloadbalancing:ModifyListener",
                "elasticloadbalancing:AddListenerCertificates",
                "elasticloadbalancing:RemoveListenerCertificates",
                "elasticloadbalancing:ModifyRule"
            ],
            "Resource": "*"
        }
    ]
}
  POLICY
}

resource "aws_iam_role" "aws-alb-management-role" {
  name        = "${var.cluster-name}-aws-load-balancer-controller"
  description = "Permissions required by the Kubernetes AWS Load Balancer controller to do its job."

  force_detach_policies = true

  assume_role_policy = data.aws_iam_policy_document.eks_oidc_assume_role[0].json
  depends_on = [aws_eks_node_group.K8S-EKS, aws_eks_cluster.K8S-EKS]
}

resource "aws_iam_role_policy_attachment" "policy-attachment" {
  policy_arn = aws_iam_policy.aws-alb-management-policy.arn
  role       = aws_iam_role.aws-alb-management-role.name
}

# resource "kubernetes_service_account" "aws-load-balancer-controller-sa" {
#   automount_service_account_token = false
#   metadata {
#     name      = "aws-load-balancer-controller"
#     namespace = var.k8s_namespace
#     annotations = {
#       "eks.amazonaws.com/role-arn" = aws_iam_role.aws-alb-management-role.arn
#     }
#     labels = {
#       "app.kubernetes.io/name"       = "aws-load-balancer-controller"
#       "app.kubernetes.io/component"  = "controller"
#       "app.kubernetes.io/managed-by" = "terraform"
#     }
#   }
#   depends_on = [aws_eks_node_group.K8S-EKS, aws_eks_cluster.K8S-EKS]
# }

# resource "kubernetes_cluster_role" "aws-load-balancer-controller-cluster-role" {
#   metadata {
#     name = "aws-load-balancer-controller"

#     labels = {
#       "app.kubernetes.io/name"       = "aws-load-balancer-controller"
#       "app.kubernetes.io/managed-by" = "terraform"
#     }
#   }

#   rule {
#     api_groups = [
#       "rbac.authorization.k8s.io",
#       "extensions",
#     ]

#     resources = [
#       "configmaps",
#       "endpoints",
#       "events",
#       "ingresses",
#       "ingresses/status",
#       "services",
#     ]

#     verbs = [
#       "create",
#       "get",
#       "list",
#       "update",
#       "watch",
#       "patch",
#     ]
#   }

#   rule {
#     api_groups = [
#       "",
#       "extensions",
#     ]

#     resources = [
#       "nodes",
#       "pods",
#       "secrets",
#       "services",
#       "namespaces",
#     ]

#     verbs = [
#       "get",
#       "list",
#       "watch",
#     ]
#   }
#   depends_on = [aws_eks_node_group.K8S-EKS, aws_eks_cluster.K8S-EKS]
# }

# resource "kubernetes_cluster_role_binding" "aws-load-balancer-controller-cluster-role-binding" {
#   metadata {
#     name = "aws-load-balancer-controller"

#     labels = {
#       "app.kubernetes.io/name"       = "aws-load-balancer-controller"
#       "app.kubernetes.io/managed-by" = "terraform"
#     }
#   }

#   role_ref {
#     api_group = "rbac.authorization.k8s.io"
#     kind      = "ClusterRole"
#     name      = kubernetes_cluster_role.aws-load-balancer-controller-cluster-role.metadata[0].name
#   }

#   subject {
#     api_group = "rbac.authorization.k8s.io"
#     kind      = "ServiceAccount"
#     name      = kubernetes_service_account.aws-load-balancer-controller-sa.metadata[0].name
#     namespace = kubernetes_service_account.aws-load-balancer-controller-sa.metadata[0].namespace
#   }
#   depends_on = [aws_eks_node_group.K8S-EKS, aws_eks_cluster.K8S-EKS]
# }

# resource "helm_release" "alb_controller" {

#   name       = "aws-load-balancer-controller"
#   repository = "https://aws.github.io/eks-charts"
#   chart      = "aws-load-balancer-controller"
#   version    = "1.4.4"
#   namespace  = var.k8s_namespace
#   atomic     = true
#   timeout    = 900
#   set {
#     name  = "replicaCount"
#     value = 1
#   }
#   values = [
#     yamlencode({
#       "clusterName" : var.cluster-name,
#       "serviceAccount" : {
#         "create" : false,
#         "name" : kubernetes_service_account.aws-load-balancer-controller-sa.metadata[0].name
#       },
#       "region" : var.aws_region,
#       "vpcId" : var.vpc_id
#       "hostNetwork" : false
#   })]
#   depends_on = [aws_eks_node_group.K8S-EKS, aws_eks_cluster.K8S-EKS]
# }
