def call(String namespace, String image) {

    echo "🚀 Deploying Application to K8s Namespace: ${namespace}"
    echo "📌 Using Image: ${image}"

    sh """
        kubectl --kubeconfig=/var/lib/jenkins/kube/config set image deployment/k8s-app k8s-container=${image} -n ${namespace}
        kubectl --kubeconfig=/var/lib/jenkins/kube/config rollout restart deployment k8s-app -n ${namespace}
        kubectl --kubeconfig=/var/lib/jenkins/kube/config rollout status deployment k8s-app -n ${namespace}
    """

    echo "🎉 Deployment Completed Successfully in → ${namespace}"
}
