# Yocto image with NFS rootfs
<img width="1920" height="1080" alt="image" src="https://github.com/user-attachments/assets/8725a31e-0855-4cd9-9909-2c692236af18" />

### That's a collection of what we did in `Embedded-Linux/EL_Task6` Repo & `00-GetStarted`

### Extracting the rootfs to nfs
'''bash
sudo tar -xjf shared-build/tmp/deploy/images/raspberrypi3-64/core-image-minimal-raspberrypi3-64.rootfs.tar.bz2 -C /srv/nfs/rootfs
'''
