SUMMARY = "app Library"

LICENSE = "MIT"
LIC_FILES_CHKSUM = "file://${COREBASE}/meta/COPYING.MIT;md5=3da9cfbcb788c80a0384361b4de20420"

SRC_URI = "file://main.c"

S = "${WORKDIR}"

DEPENDS = "ehab"

do_compile() {
    ${CC} ${CFLAGS} ${LDFLAGS} -I${STAGING_INCLUDEDIR} -L${STAGING_LIBDIR} main.c -lmath -o calculator
}

do_install() {
    install -d ${D}/usr/ehab/bin
    install -m 0755 calculator ${D}/usr/ehab/bin/
}

FILES:${PN} += "/usr/ehab/bin/calculator"