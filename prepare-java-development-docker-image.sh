#! /bin/bash -e

TARGET=java-development/build
rm -fr ${TARGET}
mkdir ${TARGET}

tar -cf - *gradle* */build.gradle buildSrc | tar -C ${TARGET} -xf -

for main in $(find */src -name *Main.java) ; do
  echo $main
  mkdir -p ${TARGET}/$(dirname $main)
  grep package $main > ${TARGET}/$main
  CLASS=$(echo $main | sed -e 's?.*/??' -e 's/\.java//' )
  cat >> ${TARGET}/$main <<END
  public class ${CLASS} {
    public static void main(String[] args) {
      // Dummy placeholder
    }
  }
END
done
