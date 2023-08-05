package com.tiny.cash.loan.card.net.response.data.bean;

public class KocHaveBean {

    private int success;
    private String action;
    private DataBean data;

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        private String kochava_device_id;
        private int retry;
        private AttributionBean attribution;

        public String getKochava_device_id() {
            return kochava_device_id;
        }

        public void setKochava_device_id(String kochava_device_id) {
            this.kochava_device_id = kochava_device_id;
        }

        public int getRetry() {
            return retry;
        }

        public void setRetry(int retry) {
            this.retry = retry;
        }

        public AttributionBean getAttribution() {
            return attribution;
        }

        public void setAttribution(AttributionBean attribution) {
            this.attribution = attribution;
        }

        public static class AttributionBean {
            private String user_agent;
            private String date;
            private int timestamp;
            private String network;
            private String network_id;
            private String network_key;
            private String legacy_tracker_id;
            private String campaign;
            private String tier;
            private String tracker;
            private String attribution_prompt;
            private String agency_network_id;
            private String tracker_id;
            private String tracker_channel_type;
            private String site;
            private String creative;
            private String country;
            private String attribution_action;
            private String install_network;
            private String install_campaign;
            private String install_tracker;
            private String install_site;
            private String install_creative;
            private String account_id;
            private String ad_id;
            private String ad_objective_name;
            private String adgroup_id;
            private String adgroup_name;
            private String attribution_module;
            private String campaign_group_id;
            private String campaign_group_name;
            private String campaign_id;
            private String campaign_name;
            private int click_time;
            private String creative_id;
            private String imp_event;
            private boolean is_external;
            private boolean is_fb;
            private boolean is_instagram;
            private boolean is_mobile_data_terms_signed;
            private boolean is_paid;
            private int lookback;
            private String matched_by;
            private String original_request;
            private String publisher_platform;
            private String site_id;
            private String tracker_type;
            private String unix_date;
            private String waterfall_level;
            private String install_matched_by;
            private ClickBean click;
            private InstallBean install;
            private String original_kochava_device_id;
            private boolean is_first_install;
            private DeferredDeeplinkBean deferred_deeplink;

            public String getUser_agent() {
                return user_agent;
            }

            public void setUser_agent(String user_agent) {
                this.user_agent = user_agent;
            }

            public String getDate() {
                return date;
            }

            public void setDate(String date) {
                this.date = date;
            }

            public int getTimestamp() {
                return timestamp;
            }

            public void setTimestamp(int timestamp) {
                this.timestamp = timestamp;
            }

            public String getNetwork() {
                return network;
            }

            public void setNetwork(String network) {
                this.network = network;
            }

            public String getNetwork_id() {
                return network_id;
            }

            public void setNetwork_id(String network_id) {
                this.network_id = network_id;
            }

            public String getNetwork_key() {
                return network_key;
            }

            public void setNetwork_key(String network_key) {
                this.network_key = network_key;
            }

            public String getLegacy_tracker_id() {
                return legacy_tracker_id;
            }

            public void setLegacy_tracker_id(String legacy_tracker_id) {
                this.legacy_tracker_id = legacy_tracker_id;
            }

            public String getCampaign() {
                return campaign;
            }

            public void setCampaign(String campaign) {
                this.campaign = campaign;
            }

            public String getTier() {
                return tier;
            }

            public void setTier(String tier) {
                this.tier = tier;
            }

            public String getTracker() {
                return tracker;
            }

            public void setTracker(String tracker) {
                this.tracker = tracker;
            }

            public String getAttribution_prompt() {
                return attribution_prompt;
            }

            public void setAttribution_prompt(String attribution_prompt) {
                this.attribution_prompt = attribution_prompt;
            }

            public String getAgency_network_id() {
                return agency_network_id;
            }

            public void setAgency_network_id(String agency_network_id) {
                this.agency_network_id = agency_network_id;
            }

            public String getTracker_id() {
                return tracker_id;
            }

            public void setTracker_id(String tracker_id) {
                this.tracker_id = tracker_id;
            }

            public String getTracker_channel_type() {
                return tracker_channel_type;
            }

            public void setTracker_channel_type(String tracker_channel_type) {
                this.tracker_channel_type = tracker_channel_type;
            }

            public String getSite() {
                return site;
            }

            public void setSite(String site) {
                this.site = site;
            }

            public String getCreative() {
                return creative;
            }

            public void setCreative(String creative) {
                this.creative = creative;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public String getAttribution_action() {
                return attribution_action;
            }

            public void setAttribution_action(String attribution_action) {
                this.attribution_action = attribution_action;
            }

            public String getInstall_network() {
                return install_network;
            }

            public void setInstall_network(String install_network) {
                this.install_network = install_network;
            }

            public String getInstall_campaign() {
                return install_campaign;
            }

            public void setInstall_campaign(String install_campaign) {
                this.install_campaign = install_campaign;
            }

            public String getInstall_tracker() {
                return install_tracker;
            }

            public void setInstall_tracker(String install_tracker) {
                this.install_tracker = install_tracker;
            }

            public String getInstall_site() {
                return install_site;
            }

            public void setInstall_site(String install_site) {
                this.install_site = install_site;
            }

            public String getInstall_creative() {
                return install_creative;
            }

            public void setInstall_creative(String install_creative) {
                this.install_creative = install_creative;
            }

            public String getAccount_id() {
                return account_id;
            }

            public void setAccount_id(String account_id) {
                this.account_id = account_id;
            }

            public String getAd_id() {
                return ad_id;
            }

            public void setAd_id(String ad_id) {
                this.ad_id = ad_id;
            }

            public String getAd_objective_name() {
                return ad_objective_name;
            }

            public void setAd_objective_name(String ad_objective_name) {
                this.ad_objective_name = ad_objective_name;
            }

            public String getAdgroup_id() {
                return adgroup_id;
            }

            public void setAdgroup_id(String adgroup_id) {
                this.adgroup_id = adgroup_id;
            }

            public String getAdgroup_name() {
                return adgroup_name;
            }

            public void setAdgroup_name(String adgroup_name) {
                this.adgroup_name = adgroup_name;
            }

            public String getAttribution_module() {
                return attribution_module;
            }

            public void setAttribution_module(String attribution_module) {
                this.attribution_module = attribution_module;
            }

            public String getCampaign_group_id() {
                return campaign_group_id;
            }

            public void setCampaign_group_id(String campaign_group_id) {
                this.campaign_group_id = campaign_group_id;
            }

            public String getCampaign_group_name() {
                return campaign_group_name;
            }

            public void setCampaign_group_name(String campaign_group_name) {
                this.campaign_group_name = campaign_group_name;
            }

            public String getCampaign_id() {
                return campaign_id;
            }

            public void setCampaign_id(String campaign_id) {
                this.campaign_id = campaign_id;
            }

            public String getCampaign_name() {
                return campaign_name;
            }

            public void setCampaign_name(String campaign_name) {
                this.campaign_name = campaign_name;
            }

            public int getClick_time() {
                return click_time;
            }

            public void setClick_time(int click_time) {
                this.click_time = click_time;
            }

            public String getCreative_id() {
                return creative_id;
            }

            public void setCreative_id(String creative_id) {
                this.creative_id = creative_id;
            }

            public String getImp_event() {
                return imp_event;
            }

            public void setImp_event(String imp_event) {
                this.imp_event = imp_event;
            }

            public boolean isIs_external() {
                return is_external;
            }

            public void setIs_external(boolean is_external) {
                this.is_external = is_external;
            }

            public boolean isIs_fb() {
                return is_fb;
            }

            public void setIs_fb(boolean is_fb) {
                this.is_fb = is_fb;
            }

            public boolean isIs_instagram() {
                return is_instagram;
            }

            public void setIs_instagram(boolean is_instagram) {
                this.is_instagram = is_instagram;
            }

            public boolean isIs_mobile_data_terms_signed() {
                return is_mobile_data_terms_signed;
            }

            public void setIs_mobile_data_terms_signed(boolean is_mobile_data_terms_signed) {
                this.is_mobile_data_terms_signed = is_mobile_data_terms_signed;
            }

            public boolean isIs_paid() {
                return is_paid;
            }

            public void setIs_paid(boolean is_paid) {
                this.is_paid = is_paid;
            }

            public int getLookback() {
                return lookback;
            }

            public void setLookback(int lookback) {
                this.lookback = lookback;
            }

            public String getMatched_by() {
                return matched_by;
            }

            public void setMatched_by(String matched_by) {
                this.matched_by = matched_by;
            }

            public String getOriginal_request() {
                return original_request;
            }

            public void setOriginal_request(String original_request) {
                this.original_request = original_request;
            }

            public String getPublisher_platform() {
                return publisher_platform;
            }

            public void setPublisher_platform(String publisher_platform) {
                this.publisher_platform = publisher_platform;
            }

            public String getSite_id() {
                return site_id;
            }

            public void setSite_id(String site_id) {
                this.site_id = site_id;
            }

            public String getTracker_type() {
                return tracker_type;
            }

            public void setTracker_type(String tracker_type) {
                this.tracker_type = tracker_type;
            }

            public String getUnix_date() {
                return unix_date;
            }

            public void setUnix_date(String unix_date) {
                this.unix_date = unix_date;
            }

            public String getWaterfall_level() {
                return waterfall_level;
            }

            public void setWaterfall_level(String waterfall_level) {
                this.waterfall_level = waterfall_level;
            }

            public String getInstall_matched_by() {
                return install_matched_by;
            }

            public void setInstall_matched_by(String install_matched_by) {
                this.install_matched_by = install_matched_by;
            }

            public ClickBean getClick() {
                return click;
            }

            public void setClick(ClickBean click) {
                this.click = click;
            }

            public InstallBean getInstall() {
                return install;
            }

            public void setInstall(InstallBean install) {
                this.install = install;
            }

            public String getOriginal_kochava_device_id() {
                return original_kochava_device_id;
            }

            public void setOriginal_kochava_device_id(String original_kochava_device_id) {
                this.original_kochava_device_id = original_kochava_device_id;
            }

            public boolean isIs_first_install() {
                return is_first_install;
            }

            public void setIs_first_install(boolean is_first_install) {
                this.is_first_install = is_first_install;
            }

            public DeferredDeeplinkBean getDeferred_deeplink() {
                return deferred_deeplink;
            }

            public void setDeferred_deeplink(DeferredDeeplinkBean deferred_deeplink) {
                this.deferred_deeplink = deferred_deeplink;
            }

            public static class ClickBean {
                private int id;
                private MetaBean meta;
                private String date;
                private int timestamp;
                private String deeplink;

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public MetaBean getMeta() {
                    return meta;
                }

                public void setMeta(MetaBean meta) {
                    this.meta = meta;
                }

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public int getTimestamp() {
                    return timestamp;
                }

                public void setTimestamp(int timestamp) {
                    this.timestamp = timestamp;
                }

                public String getDeeplink() {
                    return deeplink;
                }

                public void setDeeplink(String deeplink) {
                    this.deeplink = deeplink;
                }

                public static class MetaBean {
                    private String control_server;
                    private String original_request;

                    public String getControl_server() {
                        return control_server;
                    }

                    public void setControl_server(String control_server) {
                        this.control_server = control_server;
                    }

                    public String getOriginal_request() {
                        return original_request;
                    }

                    public void setOriginal_request(String original_request) {
                        this.original_request = original_request;
                    }
                }
            }

            public static class InstallBean {
                private MetaBean meta;
                private int id;
                private String date;
                private int timestamp;

                public MetaBean getMeta() {
                    return meta;
                }

                public void setMeta(MetaBean meta) {
                    this.meta = meta;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getDate() {
                    return date;
                }

                public void setDate(String date) {
                    this.date = date;
                }

                public int getTimestamp() {
                    return timestamp;
                }

                public void setTimestamp(int timestamp) {
                    this.timestamp = timestamp;
                }

                public static class MetaBean {
                    private String matched_on;
                    private String device;
                    private String original_usertime;
                    private String sdk_version;
                    private String install_referrer_status;
                    private String install_referrer_referrer;
                    private String install_referrer_install_begin_time;
                    private String install_referrer_referrer_click_time;
                    private String install_referrer_google_play_instant;
                    private String install_referrer_install_begin_server_time;
                    private String install_referrer_referrer_click_server_time;
                    private String install_referrer_install_version;
                    private String installed_date;
                    private String huawei_referrer_status;
                    private String app_short_string;
                    private String carrier_name;
                    private String language;
                    private String installer_package;
                    private String utm_source;
                    private String receipt_status;
                    private String device_ua;
                    private String reconciliation_request_click;
                    private String matched_by;
                    private String cpi_price;
                    private String utm_medium;
                    private String origination_ip;
                    private String in_time;
                    private String inboarder;
                    private String device_model;
                    private String marketing_name;
                    private String manufacturer;
                    private String device_ver;
                    private String device_id;
                    private String device_id_type;
                    private String alt_device_id;
                    private String advertiser_tracking_enabled;
                    private String application_tracking_enabled;
                    private String nt_id;
                    private String app_version;

                    public String getMatched_on() {
                        return matched_on;
                    }

                    public void setMatched_on(String matched_on) {
                        this.matched_on = matched_on;
                    }

                    public String getDevice() {
                        return device;
                    }

                    public void setDevice(String device) {
                        this.device = device;
                    }

                    public String getOriginal_usertime() {
                        return original_usertime;
                    }

                    public void setOriginal_usertime(String original_usertime) {
                        this.original_usertime = original_usertime;
                    }

                    public String getSdk_version() {
                        return sdk_version;
                    }

                    public void setSdk_version(String sdk_version) {
                        this.sdk_version = sdk_version;
                    }

                    public String getInstall_referrer_status() {
                        return install_referrer_status;
                    }

                    public void setInstall_referrer_status(String install_referrer_status) {
                        this.install_referrer_status = install_referrer_status;
                    }

                    public String getInstall_referrer_referrer() {
                        return install_referrer_referrer;
                    }

                    public void setInstall_referrer_referrer(String install_referrer_referrer) {
                        this.install_referrer_referrer = install_referrer_referrer;
                    }

                    public String getInstall_referrer_install_begin_time() {
                        return install_referrer_install_begin_time;
                    }

                    public void setInstall_referrer_install_begin_time(String install_referrer_install_begin_time) {
                        this.install_referrer_install_begin_time = install_referrer_install_begin_time;
                    }

                    public String getInstall_referrer_referrer_click_time() {
                        return install_referrer_referrer_click_time;
                    }

                    public void setInstall_referrer_referrer_click_time(String install_referrer_referrer_click_time) {
                        this.install_referrer_referrer_click_time = install_referrer_referrer_click_time;
                    }

                    public String getInstall_referrer_google_play_instant() {
                        return install_referrer_google_play_instant;
                    }

                    public void setInstall_referrer_google_play_instant(String install_referrer_google_play_instant) {
                        this.install_referrer_google_play_instant = install_referrer_google_play_instant;
                    }

                    public String getInstall_referrer_install_begin_server_time() {
                        return install_referrer_install_begin_server_time;
                    }

                    public void setInstall_referrer_install_begin_server_time(String install_referrer_install_begin_server_time) {
                        this.install_referrer_install_begin_server_time = install_referrer_install_begin_server_time;
                    }

                    public String getInstall_referrer_referrer_click_server_time() {
                        return install_referrer_referrer_click_server_time;
                    }

                    public void setInstall_referrer_referrer_click_server_time(String install_referrer_referrer_click_server_time) {
                        this.install_referrer_referrer_click_server_time = install_referrer_referrer_click_server_time;
                    }

                    public String getInstall_referrer_install_version() {
                        return install_referrer_install_version;
                    }

                    public void setInstall_referrer_install_version(String install_referrer_install_version) {
                        this.install_referrer_install_version = install_referrer_install_version;
                    }

                    public String getInstalled_date() {
                        return installed_date;
                    }

                    public void setInstalled_date(String installed_date) {
                        this.installed_date = installed_date;
                    }

                    public String getHuawei_referrer_status() {
                        return huawei_referrer_status;
                    }

                    public void setHuawei_referrer_status(String huawei_referrer_status) {
                        this.huawei_referrer_status = huawei_referrer_status;
                    }

                    public String getApp_short_string() {
                        return app_short_string;
                    }

                    public void setApp_short_string(String app_short_string) {
                        this.app_short_string = app_short_string;
                    }

                    public String getCarrier_name() {
                        return carrier_name;
                    }

                    public void setCarrier_name(String carrier_name) {
                        this.carrier_name = carrier_name;
                    }

                    public String getLanguage() {
                        return language;
                    }

                    public void setLanguage(String language) {
                        this.language = language;
                    }

                    public String getInstaller_package() {
                        return installer_package;
                    }

                    public void setInstaller_package(String installer_package) {
                        this.installer_package = installer_package;
                    }

                    public String getUtm_source() {
                        return utm_source;
                    }

                    public void setUtm_source(String utm_source) {
                        this.utm_source = utm_source;
                    }

                    public String getReceipt_status() {
                        return receipt_status;
                    }

                    public void setReceipt_status(String receipt_status) {
                        this.receipt_status = receipt_status;
                    }

                    public String getDevice_ua() {
                        return device_ua;
                    }

                    public void setDevice_ua(String device_ua) {
                        this.device_ua = device_ua;
                    }

                    public String getReconciliation_request_click() {
                        return reconciliation_request_click;
                    }

                    public void setReconciliation_request_click(String reconciliation_request_click) {
                        this.reconciliation_request_click = reconciliation_request_click;
                    }

                    public String getMatched_by() {
                        return matched_by;
                    }

                    public void setMatched_by(String matched_by) {
                        this.matched_by = matched_by;
                    }

                    public String getCpi_price() {
                        return cpi_price;
                    }

                    public void setCpi_price(String cpi_price) {
                        this.cpi_price = cpi_price;
                    }

                    public String getUtm_medium() {
                        return utm_medium;
                    }

                    public void setUtm_medium(String utm_medium) {
                        this.utm_medium = utm_medium;
                    }

                    public String getOrigination_ip() {
                        return origination_ip;
                    }

                    public void setOrigination_ip(String origination_ip) {
                        this.origination_ip = origination_ip;
                    }

                    public String getIn_time() {
                        return in_time;
                    }

                    public void setIn_time(String in_time) {
                        this.in_time = in_time;
                    }

                    public String getInboarder() {
                        return inboarder;
                    }

                    public void setInboarder(String inboarder) {
                        this.inboarder = inboarder;
                    }

                    public String getDevice_model() {
                        return device_model;
                    }

                    public void setDevice_model(String device_model) {
                        this.device_model = device_model;
                    }

                    public String getMarketing_name() {
                        return marketing_name;
                    }

                    public void setMarketing_name(String marketing_name) {
                        this.marketing_name = marketing_name;
                    }

                    public String getManufacturer() {
                        return manufacturer;
                    }

                    public void setManufacturer(String manufacturer) {
                        this.manufacturer = manufacturer;
                    }

                    public String getDevice_ver() {
                        return device_ver;
                    }

                    public void setDevice_ver(String device_ver) {
                        this.device_ver = device_ver;
                    }

                    public String getDevice_id() {
                        return device_id;
                    }

                    public void setDevice_id(String device_id) {
                        this.device_id = device_id;
                    }

                    public String getDevice_id_type() {
                        return device_id_type;
                    }

                    public void setDevice_id_type(String device_id_type) {
                        this.device_id_type = device_id_type;
                    }

                    public String getAlt_device_id() {
                        return alt_device_id;
                    }

                    public void setAlt_device_id(String alt_device_id) {
                        this.alt_device_id = alt_device_id;
                    }

                    public String getAdvertiser_tracking_enabled() {
                        return advertiser_tracking_enabled;
                    }

                    public void setAdvertiser_tracking_enabled(String advertiser_tracking_enabled) {
                        this.advertiser_tracking_enabled = advertiser_tracking_enabled;
                    }

                    public String getApplication_tracking_enabled() {
                        return application_tracking_enabled;
                    }

                    public void setApplication_tracking_enabled(String application_tracking_enabled) {
                        this.application_tracking_enabled = application_tracking_enabled;
                    }

                    public String getNt_id() {
                        return nt_id;
                    }

                    public void setNt_id(String nt_id) {
                        this.nt_id = nt_id;
                    }

                    public String getApp_version() {
                        return app_version;
                    }

                    public void setApp_version(String app_version) {
                        this.app_version = app_version;
                    }
                }
            }

            public static class DeferredDeeplinkBean {
                private String destination;
                private boolean deferred;

                public String getDestination() {
                    return destination;
                }

                public void setDestination(String destination) {
                    this.destination = destination;
                }

                public boolean isDeferred() {
                    return deferred;
                }

                public void setDeferred(boolean deferred) {
                    this.deferred = deferred;
                }
            }
        }
    }
}
