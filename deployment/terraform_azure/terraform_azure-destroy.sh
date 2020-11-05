#! /bin/bash -e

cd "$( cd "$( dirname "${BASH_SOURCE[0]}" )" >/dev/null 2>&1 && pwd )"

terraform destroy -auto-approve
