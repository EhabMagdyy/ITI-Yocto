SUMMARY = "Math Library"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://mymath.c \
           file://mymath.h"

S = "${WORKDIR}"

PROVIDES += "ehab"

do_compile(){
    ${CC} ${CFLAGS} -c mymath.c -o mymath.o
    ${AR} rcs libmath.a mymath.o
}

do_install(){
    install -d ${D}${libdir}
    install -m 0755 ${B}/libmath.a ${D}${libdir}/
    install -d ${D}${includedir}
    install -m 0755 mymath.h ${D}${includedir}/
}
