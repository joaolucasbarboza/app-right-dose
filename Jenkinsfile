pipeline {
	agent any

	environment {
		SPRING_AI_OLLAMA_BASE_URL = credentials('SPRING_AI_OLLAMA_BASE_URL')
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

		FIREBASE_SA_B64 = credentials('FIREBASE_SA_B64')
	}

	stages {
		stage('Setup Firebase') {
			steps {
				script {
					withCredentials([string(credentialsId: 'FIREBASE_SA_B64', variable: 'FIREBASE_SA')]) {
						writeFile file: 'firebase-service-account.json', text: sh(
							script: "echo \$FIREBASE_SA | base64 -d",
							returnStdout: true
						)
					}
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
					def newContainer = 'right-dose-new'
					def finalContainer = 'right-dose'
					def maxRetries = 5
					def retryInterval = 5

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

					docker.image(env.DOCKER_IMAGE).withRun("${envVars} -v \${WORKSPACE}/firebase-service-account.json:/app/firebase-service-account.json:ro -p 8080:8080 --name ${newContainer}") { c ->
						def healthy = false
						echo "Verificando health check do novo container..."

						for (int i = 0; i < maxRetries; i++) {
							try {
								def health = sh(script: "curl -f http://localhost:8080/actuator/health || exit 1", returnStatus: true)
								if (health == 0) {
									healthy = true
									break
								}
							} catch (Exception e) {
								echo "Health check tentativa ${i+1} falhou, tentando novamente em ${retryInterval} segundos..."
							}
							sleep retryInterval
						}

						if (!healthy) {
							error "Health check falhou após ${maxRetries} tentativas. Abortando deploy."
						}

						echo "Novo container está saudável, removendo container antigo..."

						def oldContainer = docker.image(env.DOCKER_IMAGE).inspect(finalContainer)
						if (oldContainer) {
							docker.image(env.DOCKER_IMAGE).stop(finalContainer)
							docker.image(env.DOCKER_IMAGE).remove(finalContainer)
						}

						sh "docker rename ${newContainer} ${finalContainer}"
						echo "Deploy concluído com sucesso!"
					}
				}
			}
		}
	}

	post {
		always {
			sh 'rm -f firebase-service-account.json'
		}
	}
}
