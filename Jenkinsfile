#!/usr/bin/env groovy

pipeline {
    agent any
    tools {
        jdk 'java16'
    }
    stages {
        stage('Clean') {
            steps {
                echo 'Cleaning Project'
                sh 'chmod +x gradlew'
                sh './gradlew clean'
            }
        }

        stage('Build') {
            steps {
                echo 'Building'
                sh './gradlew build'
            }
        }

        stage('Archive artifacts') {
            steps {
                echo 'Archive'
                archiveArtifacts 'build/libs*/*jar'
            }
        }

       stage('Upload artifacts to CurseForge') {
           steps {
               echo 'Uploading to CurseForge'
               sh './gradlew curseforge'
           }
       }

        stage('Publish artifacts') {
            steps {
                echo 'Publishing'
                sh './gradlew publish'
            }
        }
    }
}
