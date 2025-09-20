pipeline {
	agent any

	stages {
		stage('Build Docker Image') {
			steps {
				script {
					def dockerapp = docker.build("joaolucasbarboza/right-dose:${env.BUILD_ID}", '-f Dockerfile')
					env.DOCKER_IMAGE = "joaolucasbarboza/right-dose:${env.BUILD_ID}"
				}
			}
		}

		stage('Push Docker Image') {
			steps {
				script {
					def dockerapp = docker.image(env.DOCKER_IMAGE)
					docker.withRegistry('https://registry.hub.docker.com', 'dockerhub-credentials') {
						dockerapp.push('latest')
						dockerapp.push("${env.BUILD_ID}")
					}
				}
			}
		}

		stage('Deploy Docker Image') {
			steps {
				sh 'echo "Deployment Docker Image"'
			}
		}
	}
}
