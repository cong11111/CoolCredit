package com.tiny.cash.loan.card.net;

import com.tiny.cash.loan.card.net.api.ApiService;
import com.tiny.cash.loan.card.net.server.ApiServerImpl;

public class NetManager {
    public static ApiService getApiService() {
        return ApiServerImpl.getInstance().createApi(ApiService.class);
    }

}
