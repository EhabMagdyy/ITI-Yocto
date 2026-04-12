SUMMARY = "Simple application that prints a greeting message"

# License information for the recipe. Required field
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "git://github.com/ayman4105/test_yocto.git;protocol=https;branch=main \
           file://0001-fix-added-Ehab.patch \
          "

# get a specific commit SHA
SRCREV = "3c801282b99e0f40ebd044bb3fd7c2e03b8b09a5"

# Where the source lands after fetch
S = "${WORKDIR}/git"

# Compile the application
# ${CC} Cross-compiler provided by Yocto, ${CFLAGS} ${LDFLAGS} Always pass these because Yocto injects hardening/sysroot flags here
do_compile(){
    ${CC} ${CFLAGS} ${LDFLAGS} main.c -o ehab
}

# install in bindir => /usr/bin, -d Create the directory if it doesn't exist, -m 0755 => set permissions to rwxr-xr-x
do_install(){
    install -d ${D}${bindir}
    install -m 0755 ehab ${D}${bindir}
}
