pipeline {
	agent any

	stages {
		stage('Build Dcoker Image') {
			steps {
				sh 'echo "Building Docker Image"'
			}
		}

		stage('Push Dcoker Image') {
			steps {
				sh 'echo "Pushing Docker Image"'
			}
		}

		stage('Deploy Dcoker Image') {
			steps {
				sh 'echo "Deployment Docker Image"'
			}
		}
	}
}