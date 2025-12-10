def call(Map config = [:]) {

    // Read values from the map
    def image = config.image
    def namespace = config.namespace ?: "dev"
    def kubeCredId = config.kubeconfigCredentialId ?: "k8s-config"

    if (!image) {
        error "k8sDeploy: 'image' parameter is required"
    }

    echo "🚀 Deploying image: ${image} to namespace: ${namespace}"
    echo "🌐 Using kubeconfig credential: ${kubeCredId}"

    withCredentials([file(credentialsId: kubeCredId, variable: 'KCFG')]) {
        sh """
            # Prepare kubeconfig path
            mkdir -p /var/lib/jenkins/kube
            cp \$KCFG /var/lib/jenkins/kube/config
            chmod 600 /var/lib/jenkins/kube/config

            # Replace IMAGE placeholder in deployment.yaml
            sed -i 's|IMAGE|${image}|g' k8s/deployment.yaml

            # Apply to Kubernetes
            kubectl --kubeconfig=/var/lib/jenkins/kube/config apply -f k8s/deployment.yaml -n ${namespace}
            kubectl --kubeconfig=/var/lib/jenkins/kube/config get pods -n ${namespace}
        """
    }
}

