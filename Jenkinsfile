pipeline {
    agent any
    parameters {
        string(name: 'param1', defaultValue: 'val1', description: 'desc1')
        string(name: 'param2', defaultValue: 'val2', description: 'desc2')
    }
    stages {
        stage('stage1') {
            steps {
                echo "stage1 hello"
                echo "param1: ${params.param1}"
                echo "param2: ${params.param2}"
            }
        }
        stage('stage2') {
            steps {
                echo "stage 2 hello"
            }
        }
    }
}
