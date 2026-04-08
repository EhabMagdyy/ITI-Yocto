# Building Recipe that compiles & installs an app in /usr/bin
<img width="933" height="415" alt="Screenshot from 2026-04-09 00-38-46" src="https://github.com/user-attachments/assets/44e5606b-5560-4549-ac70-f12b53c3d379" />

## 1. Create Layer
 
```bash
bitbake-layers create-layer meta-weather
bitbake-layers add-layer meta-weather
```
 
---
 
## 2. Create Recipe Structure
 
```
meta-weather/
└── recipes-apps/
    └── sayhi/
        ├── sayhi.bb
        └── files/
            └── sayhi.c     <= your C source goes here
```

---

## 3. Append for testing

``` bitbake
# in conf/layer.conf (not good, just for testing):
IMAGE_INSTALL:append = " sayhi"
```

---
 
## 4. Build
 
```bash
bitbake sayhi
bitbake core-image-minimal -k
# from shared-build/tmp/deploy/images/raspberrypi3-64
sudo bmaptool copy core-image-minimal-raspberrypi3-64.rootfs-20260408223323.wic.bz2 /dev/sdb
```
 
