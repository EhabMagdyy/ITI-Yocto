SUMMARY = "Simple application that prints a greeting message"

# License information for the recipe. Required field
LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://sayhi.c"

# Where the source lands after fetch
S = "${WORKDIR}"

# Compile the application
# ${CC} Cross-compiler provided by Yocto, ${CFLAGS} ${LDFLAGS} Always pass these because Yocto injects hardening/sysroot flags here
do_compile(){
    ${CC} ${CFLAGS} ${LDFLAGS} sayhi.c -o sayhi
}

# install in bindir => /usr/bin, -d Create the directory if it doesn't exist, -m 0755 => set permissions to rwxr-xr-x
do_install(){
    install -d ${D}${bindir}
    install -m 0755 sayhi ${D}${bindir}
}

# Build the recipe: bitbake sayhi
# Check what's inside the produced package: oe-pkgdata-util list-pkg-files sayhi