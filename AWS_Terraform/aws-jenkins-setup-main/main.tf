terraform {
  required_providers {
    aws = {
      source  = "hashicorp/aws"
      version = "~> 5.0"
    }
  }
}

provider "aws" {
  region = "us-east-1"
}

# Below resource is to create public key
resource "tls_private_key" "sskeygen_execution" {
  algorithm = "RSA"
  rsa_bits  = 4096
}


# Below are the aws key pair
resource "aws_key_pair" "jenkins_key_pair" {
  depends_on = [tls_private_key.sskeygen_execution]
  key_name   = "jenkins_aws_rsa"
  public_key = tls_private_key.sskeygen_execution.public_key_openssh
}

resource "aws_security_group" "jenkins_sg" {
  name        = "jenkins_sg"
  description = "Allow ssh and HTTP trafic"
  vpc_id      = "vpc-002943b43a19be740"

  ingress {
    from_port   = 22
    to_port     = 22
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 8080
    to_port     = 8080
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port   = 443
    to_port     = 443
    protocol    = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }

  egress {
    from_port   = 0
    to_port     = 0
    protocol    = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_iam_role" "jenkins-iam-role" {
  name = "jenkins-iam-role"

  assume_role_policy = jsonencode({
    Version = "2012-10-17"
    Statement = [
      {
        Effect = "Allow"
        Principal = {
          Service = "ec2.amazonaws.com"
        }
        Action = "sts:AssumeRole"
      }
    ]
  })
}

# to attach admin policy to role
resource "aws_iam_role_policy_attachment" "AdministratorAccess" {
  policy_arn = "arn:aws:iam::aws:policy/AdministratorAccess"
  role       = aws_iam_role.jenkins-iam-role.name
}

# to create profile for role
resource "aws_iam_instance_profile" "jenkins_ec2_to_profile" {
  name = "jenkins_ec2_profile"
  role = aws_iam_role.jenkins-iam-role.name
}

# attach ecr policy to grant ecr access to role
resource "aws_iam_role_policy_attachment" "jenkins_ec2_to_ecr" {
  policy_arn = "arn:aws:iam::aws:policy/EC2InstanceProfileForImageBuilderECRContainerBuilds"
  role       = aws_iam_role.jenkins-iam-role.name
}

resource "aws_instance" "jenkins_ec2" {
  ami                  = "ami-051f7e7f6c2f40dc1"
  instance_type        = "t2.small"
  security_groups      = [aws_security_group.jenkins_sg.name]
  iam_instance_profile = aws_iam_instance_profile.jenkins_ec2_to_profile.name

  key_name = aws_key_pair.jenkins_key_pair.id

  tags = {
    Name = "Jenkins"
  }

  user_data = "${file("start.sh")}"

  
}

resource "aws_s3_bucket" "jenkins_s3" {
  bucket = "jenkins-nehsharm22-s3-eca"
}
