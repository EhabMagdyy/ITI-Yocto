SUMMARY = "My Custom Yocto Image"

inherit core-image

IMAGE_FEATURES:append = " ssh-server-dropbear debug-tweaks"
IMAGE_INSTALL:append = " python3 tcpdump connman-client"

# Minimum 12MB
IMAGE_ROOTFS_SIZE ?= "12288"
# add 50% overhead to the rootfs size to account for metadata and other overhead
IMAGE_OVERHEAD_FACTOR = "1.5"

IMAGE_FSTYPES = "ext3 tar.bz2 wic.bz2"
