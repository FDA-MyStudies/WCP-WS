package com.hphc.mystudies.dto;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "version_info")
public class VersionInfoDTO implements Serializable {

    private static final long serialVersionUID = 135353554543L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "version_info_id")
    private Integer versionInfoId;

    @Column(name = "android")
    private String androidVersion;

    @Column(name = "ios")
    private String iosVersion;

    @Column(name = "android_force_update")
    private Boolean androidForceUpgrade;

    @Column(name = "ios_force_update")
    private Boolean iosForceUpgrade;

    @Column(name = "app_id")
    private String appId;

    @Column(name = "app_name")
    private String appName;

    @Column(name = "org_id")
    private String orgId;

    public Integer getVersionInfoId() {
        return versionInfoId;
    }

    public void setVersionInfoId(Integer versionInfoId) {
        this.versionInfoId = versionInfoId;
    }

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public String getIosVersion() {
        return iosVersion;
    }

    public void setIosVersion(String iosVersion) {
        this.iosVersion = iosVersion;
    }

    public Boolean getAndroidForceUpgrade() {
        return androidForceUpgrade;
    }

    public void setAndroidForceUpgrade(Boolean androidForceUpgrade) {
        this.androidForceUpgrade = androidForceUpgrade;
    }

    public Boolean getIosForceUpgrade() {
        return iosForceUpgrade;
    }

    public void setIosForceUpgrade(Boolean iosForceUpgrade) {
        this.iosForceUpgrade = iosForceUpgrade;
    }

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getOrgId() {
        return orgId;
    }

    public void setOrgId(String orgId) {
        this.orgId = orgId;
    }
}
