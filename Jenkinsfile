pipeline {
	agent any

	stages {
		stage('Build Docker Image') {
			steps {
				script {
					dockerapp = docker.build("joaolucasbarboza/right-dose:${env.BUILD_ID}", '-f Dockerfile .')
				}
			}
		}

		stage('Push Dcoker Image') {
			steps {
				script {
					docker.withRegistry('https://registry.hub.docker.com', 'dockerhub-credentials') {
						dockerapp.push('latest')
						dockerapp.push("${env.BUILD_ID}")
					}
				}
			}
		}

		stage('Deploy Dcoker Image') {
			steps {
				sh 'echo "Deployment Docker Image"'
			}
		}
	}
}