pipeline {
	agent any

	tools {
		maven 'Maven 3.9.11'
  	}

	environment {
		SPRING_AI_OLLAMA_BASE_URL = credentials('SPRING_AI_OLLAMA_BASE_URL')
		SPRING_GEMINI_API_BASE_URL = credentials('SPRING_GEMINI_API_BASE_URL')
		SPRING_GEMINI_API_KEY = credentials('SPRING_GEMINI_API_KEY')
		SPRING_GEMINI_API_MODEL = credentials('SPRING_GEMINI_API_MODEL')

		SPRING_APPLICATION_NAME = credentials('SPRING_APPLICATION_NAME')
		SPRING_PROFILES_ACTIVE = credentials('SPRING_PROFILES_ACTIVE')
		SERVER_PORT = '8080'
		JAVA_OPTS = '-XX:+UseG1GC -XX:MaxRAMPercentage=75'
		JWT_SECRET = credentials('JWT_SECRET')
		KEY_OPENAI = credentials('KEY_OPENAI')

		POSTGRES_DB = credentials('POSTGRES_DB')
		POSTGRES_USER = credentials('POSTGRES_USER')
		POSTGRES_PASSWORD = credentials('POSTGRES_PASSWORD')
		SPRING_DATASOURCE_URL = "jdbc:postgresql://postgres:5432/${POSTGRES_DB}"
		SPRING_DATASOURCE_USERNAME = credentials('SPRING_DATASOURCE_USERNAME')
		SPRING_DATASOURCE_PASSWORD = credentials('SPRING_DATASOURCE_PASSWORD')

		RABBITMQ_DEFAULT_USER = credentials('RABBITMQ_DEFAULT_USER')
		RABBITMQ_DEFAULT_PASS = credentials('RABBITMQ_DEFAULT_PASS')
		SPRING_RABBITMQ_HOST = 'rabbitmq'
		SPRING_RABBITMQ_PORT = '5672'
		SPRING_RABBITMQ_USERNAME = credentials('RABBITMQ_DEFAULT_USER')
		SPRING_RABBITMQ_PASSWORD = credentials('RABBITMQ_DEFAULT_PASS')
	}

	stages {
		stage('Setup Firebase') {
			steps {
				withCredentials([file(credentialsId: 'FIREBASE_SA', variable: 'FIREBASE_SA_FILE')]) {
				sh '''
					cp "$FIREBASE_SA_FILE" "$WORKSPACE/firebase-service-account.json"
					chmod 600 "$WORKSPACE/firebase-service-account.json"
				'''
				}
  			}
		}

		stage('SCM') {
			steps { checkout scm }
    	}

    	stage('SonarQube Analysis') {
			steps {
				withSonarQubeEnv('sonarqube') {
          			sh 'mvn -B clean verify sonar:sonar -Dsonar.projectKey=right-dose -Dsonar.projectName=right-dose'
        		}
      		}
    	}

    	stage('Quality Gate') {
			steps {
				timeout(time: 10, unit: 'MINUTES') {
					waitForQualityGate abortPipeline: true
    			}
  			}
		}

		stage('Build Docker Image') {
			steps {
				script {
					def dockerapp = docker.build("joaolucaas/right-dose:${env.BUILD_ID}", '-f Dockerfile .')
					env.DOCKER_IMAGE = "joaolucaas/right-dose:${env.BUILD_ID}"
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
				script {
					def network = 'right-dose_default'
					def nameContainer = 'right-dose'
					def maxRetries = 5
					def retryInterval = 20

					docker

					sh """
						if docker ps -a | grep -q ${nameContainer}; then
							echo "Removendo container antigo..."
							docker stop ${nameContainer} || true
							docker rm ${nameContainer} || true
						fi
					"""

					def envVars = """
						-e SPRING_AI_OLLAMA_BASE_URL=${SPRING_AI_OLLAMA_BASE_URL}
						-e SPRING_APPLICATION_NAME=${SPRING_APPLICATION_NAME}
						-e SPRING_PROFILES_ACTIVE=${SPRING_PROFILES_ACTIVE}
						-e SERVER_PORT=${SERVER_PORT}
						-e JAVA_OPTS='${JAVA_OPTS}'
						-e JWT_SECRET=${JWT_SECRET}
						-e KEY_OPENAI=${KEY_OPENAI}
						-e POSTGRES_DB=${POSTGRES_DB}
						-e POSTGRES_USER=${POSTGRES_USER}
						-e POSTGRES_PASSWORD=${POSTGRES_PASSWORD}
						-e SPRING_DATASOURCE_URL=${SPRING_DATASOURCE_URL}
						-e SPRING_DATASOURCE_USERNAME=${SPRING_DATASOURCE_USERNAME}
						-e SPRING_DATASOURCE_PASSWORD=${SPRING_DATASOURCE_PASSWORD}
						-e RABBITMQ_DEFAULT_USER=${RABBITMQ_DEFAULT_USER}
						-e RABBITMQ_DEFAULT_PASS=${RABBITMQ_DEFAULT_PASS}
						-e SPRING_RABBITMQ_HOST=${SPRING_RABBITMQ_HOST}
						-e SPRING_RABBITMQ_PORT=${SPRING_RABBITMQ_PORT}
						-e SPRING_RABBITMQ_USERNAME=${SPRING_RABBITMQ_USERNAME}
						-e SPRING_RABBITMQ_PASSWORD=${SPRING_RABBITMQ_PASSWORD}
					""".trim().replaceAll('\n\\s+', ' ')

					sh """
						echo "Iniciando novo container ${nameContainer}"
						docker run -d \
							--network ${network} \
							--name ${nameContainer} ${envVars} \
							-v $WORKSPACE/firebase-service-account.json:/app/firebase-service-account.json:ro \
							-p 8080:8080 ${env.DOCKER_IMAGE} \
					"""

					sh """
						echo 'Verificando health do ${nameContainer}...'
						for i in \$(seq 1 ${maxRetries}); do
						  if docker exec ${nameContainer} sh -c 'curl -fsS http://127.0.0.1:8080/actuator/health | grep -q "UP"'; then
							echo 'Health OK'
							exit 0
						  fi
						  echo "Tentativa \$i/${maxRetries} falhou; aguardando ${retryInterval}s..."
						  sleep ${retryInterval}
						done
						echo 'Health FAILED'; docker logs --tail=200 ${nameContainer} || true; exit 1
					"""
				}
			}
		}
	}

	post {
		always {
			sh 'rm -f "$WORKSPACE/firebase-service-account.json" || true'
		}
	}
}
