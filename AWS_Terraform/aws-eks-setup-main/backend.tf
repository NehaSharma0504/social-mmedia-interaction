terraform {
  backend "s3" {
    bucket = "base-stack-configbucket-18mzutrlf0y4n"
    key    = "npglobalintegrationv2.tfstate"
    region = "us-east-1"
    profile = "default"
    access_key = "xxx"
    secret_key = "xxx"
  }
}

