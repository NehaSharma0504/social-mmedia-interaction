#!/bin/bash
sudo yum update -y

#Download Jenkins
sudo wget -O /etc/yum.repos.d/jenkins.repo https://pkg.jenkins.io/redhat-stable/jenkins.repo
sudo rpm --import https://pkg.jenkins.io/redhat-stable/jenkins.io-2023.key

#Install Java
sudo yum upgrade -y
sudo dnf install java-11-amazon-corretto

#Install Git
yum install git -y
# which git

#Install Maven
sudo yum install wget maven -y
# mvn -version

#Install Jenkins
sudo yum install jenkins -y
sudo systemctl enable jenkins
sudo systemctl start jenkins
# sudo systemctl status jenkins

#Install Docker
sudo yum install -y docker
sudo systemctl enable docker
sudo systemctl start docker
# sudo systemctl status docker

#Add user with docker
sudo usermod -a -G docker jenkins
sudo chmod 777 /var/run/docker.sock

#kubectl
sudo curl -LO "https://dl.k8s.io/release/$(curl -L -s https://dl.k8s.io/release/stable.txt)/bin/linux/amd64/kubectl"
sudo install -o root -g root -m 0755 kubectl /usr/local/bin/kubectl
# kubectl version --client --output=yaml

#Helm
sudo curl -fsSL -o get_helm.sh https://raw.githubusercontent.com/helm/helm/main/scripts/get-helm-3
sudo chmod 700 get_helm.sh
sudo ./get_helm.sh
# helm version

#Restart Jenkins
service jenkins restart

#Demon Reload
systemctl daemon-reload

#Restart Docker
systemctl stop docker
systemctl start docker

#Jenkins password
# sudo cat /var/lib/jenkins/secrets/initialAdminPassword
