kubectl config use-context arn:aws:eks:us-east-1:477658251325:cluster/usvote-dev-k8s

COMMAND=${1:?"no COMMAND specified. should be one of 'shutdown' 'start'"}
  if [[ "${COMMAND}" == "shutdown" ]]; then
   echo "scaling node group to zero" 

  eksctl scale nodegroup --profile=usvote --cluster=usvote-dev-k8s --nodes=0 --name=usvote-dev-k8s  --nodes-min=0  --nodes-max=3 
kubectl get nodes 

  elif [[ "${COMMAND}" == "start" ]]; then
  
  echo "scaling up node group " 
  eksctl scale nodegroup --profile=usvote --cluster=usvote-dev-k8s --nodes=1 --name=usvote-dev-k8s  --nodes-min=0  --nodes-max=3 
kubectl get nodes 
  else
    echo "invalid command passed"
    exit 1
  fi;
  echo "${COMMAND} done"
  exit 1

