# ase-delivery

This is the final ASEDelivery project containing backend, frontend, and hardware.


## Deployment and Execution

The project contains an established CI/CD pipeline. Therefore, the deployment to AWS is possible within a few steps.

Our final working RELEASE can be found here:
`https://gitlab.lrz.de/ase-21-22/team-5/ase-delivery/-/tags/RELEASE`

1. Configure the CI/CD variables `AWS_USER_HOST` to indicate your AWS host and AWS_PEM_KEY which contains your SSH private key (content of .pem file) in ASE 21-22/Team 5/Settings/CI/CD. The other variable should stay the same.

2. Run the pipeline without any input variables. The containers are built, packaged, pushed to the container registry, and finally deployed to a custom AWS host. We excluded the test stage since we have not implemented any tests and wanted to speed up the pipeline but it is available if you comment the corresponding code section out.

3. In the last stage the code is deployed to AWS. We recognized that due to the limited server capabilities the AWS instance is not accessible via SSH or by connecting to the console via the web interface if we would include the last command in the stage (`ssh $AWS_USER_HOST "sudo docker-compose -f $DOCKER_COMPOSE_FILE up --force-recreate -d`). Therefore, we decided to push the docker compose and pull the necessary containers to the AWS instance from the gitlab container registry.

4. To run the instance connect to your AWS instance and execute the command "sudo docker-compose up". Now after the server runs you can access the webpage at `<Public IPv4 DNS>:3000`.
