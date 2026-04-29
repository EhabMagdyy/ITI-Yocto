SUMMARY = "My Custom Yocto Image"

inherit core-image extrausers

IMAGE_FEATURES:append = " ssh-server-dropbear"
IMAGE_INSTALL:append = " weston weston-init"
IMAGE_INSTALL:append = " python3 tcpdump sudo custom-connectivity nano \
                         qtbase qtbase-plugins qtdeclarative qtdeclarative-qmlplugins qtmultimedia \
                         libvncserver"

IMAGE_INSTALL:append = " libdrm"

IMAGE_ROOTFS_SIZE ?= "102400"
# add 50% overhead to the rootfs size to account for metadata and other overhead
IMAGE_OVERHEAD_FACTOR = "1.5"

# Define the image types to be generated
IMAGE_FSTYPES = "ext4 tar.bz2 wic.bz2"

# Add sudo permissions for the "iti" user
set_sudo_permissions() {
    if [ -e ${IMAGE_ROOTFS}${sysconfdir}/sudoers ]; then
        echo '%sudo ALL=(ALL) ALL' >> ${IMAGE_ROOTFS}${sysconfdir}/sudoers
    fi
}
ROOTFS_POSTPROCESS_COMMAND += "set_sudo_permissions; "
