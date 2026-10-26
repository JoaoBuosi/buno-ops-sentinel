terraform {
 required_version=">= 1.5.0"
 required_providers { aws={source="hashicorp/aws",version="~> 5.0"} }
}
provider "aws" { region=var.region }
variable "region" { type=string; default="us-east-1" }
variable "environment" { type=string; default="dev" }
resource "aws_sns_topic" "alerts" { name="buno-ops-alerts-${var.environment}" }
output "alerts_topic_arn" { value=aws_sns_topic.alerts.arn }
