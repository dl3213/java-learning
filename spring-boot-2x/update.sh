if [ ! -d "./temp/" ];then
  mkdir./temp
fi
echo " ==> $0"
echo " ==> $1"
git log --since="$1" --name-only | grep "src/" | sed "s/\.java/\.class/g" | sed "s/src\/main\/java/\.\/target\/classes/g" | xargs cp --parent -t ./temp
