
echo " ==> $0"
echo " ==> $1"

git_file_list=$(git log --since='$1' --name-only | grep 'src')

echo "$git_file_list"

echo " ========> "
# 替换.java => .class
# 替换src/main/java => target/classes -- sed 不会改变字符串,所以需要echo来接收结果再赋值
target_file_list=$( echo "$git_file_list" | sed 's/\.java/\.class/g' | sed 's/src\/main\/java/\.\/target\/classes/g')
echo "$target_file_list"
echo "copy ing......"

# 创建临时文件夹
if [ ! -d "./temp/" ];then
  mkdir ./temp
else
  echo ""
fi

echo "$target_file_list" | xargs cp --parent -t ./temp
echo "copy done......"
#git log --since="$1" --name-only | grep "src/" | sed "s/\.java/\.class/g" | sed "s/src\/main\/java/\.\/target\/classes/g" | xargs cp --parent -t ./temp

