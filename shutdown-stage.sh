kubectl config use-context arn:aws:eks:us-east-1:477658251325:cluster/usvote-stage-eks

COMMAND=${1:?"no COMMAND specified. should be one of 'shutdown' 'start'"}
  if [[ "${COMMAND}" == "shutdown" ]]; then
   echo "scaling node group to zero" 

  eksctl scale nodegroup --profile=usvote --cluster=usvote-stage-eks --nodes=0 --name=usvote-stage  --nodes-min=0  --nodes-max=3 

  kubectl get nodes 

  elif [[ "${COMMAND}" == "start" ]]; then
  
  echo "scaling up node group"
 
  eksctl scale nodegroup --profile=usvote --cluster=usvote-stage-eks --nodes=1 --name=usvote-stage  --nodes-min=0  --nodes-max=3 

kubectl get nodes 
  else
    echo "invalid command passed"
    exit 1
  fi;
  echo "${COMMAND} done"
  exit 1 