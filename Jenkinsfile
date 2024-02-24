pipeline{
    agent any
    tools{
        gradle 'Gradle 8.5'
    }
    stages{
        stage('Build'){
            steps{
                checkout scmGit(branches: [[name: '*/master']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/CHISANW/message-board']])
                bat 'gradle clean build'
            }
        }
        stage('Docker build image'){
            steps{
                script{
                    bat 'docker build -t keuye0638/board_springboot:1.0 .'
                }
            }
        }
        stage('Docker hub image'){
            steps{
                script{
                    withCredentials([string(credentialsId: 'dockerpwd', variable: 'dockerpwd')]) {
                        bat "docker login -u keuye0638 -p $dockerpwd"
                    }
                    bat 'docker push keuye0638/board_springboot:1.0'
                }
            }
        }
        stage('Docker run Cotainier'){
            steps{
                script{
                    bat 'docker rm -f board-con || true'
                    bat ' docker run -d -p 8000:8080 --network testServer --name board-con keuye0638/board_springboot:1.0'
                }
            }
        }
    }
}