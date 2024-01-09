terraform {
  backend "s3" {
    bucket = "base-stack-configbucket-18mzutrlf0y4n"
    key    = "npglobalintegrationv2.tfstate"
    region = "us-east-1"
    profile = "default"
    access_key = "AKIAQPGA7FTW2L6N4NE3"
    secret_key = "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"
  }
}

