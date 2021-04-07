package cn.syned.p2p.config;

import com.alipay.easysdk.kernel.Config;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AliPayConfig {
    @Bean
    public Config initAliPayConfig() {
        Config config = new Config();
        config.protocol = "https";
        config.gatewayHost = "openapi.alipaydev.com";
        config.signType = "RSA2";
        config.appId = "2021000117627136";
        config.alipayPublicKey = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAgwAhfjIWR0z6463HZLMjN+E79oGoZ5ioIvbiyTH1O2Uo4pY7eqfk8w+xSNJbIuaS0EdpWj/s3k5ILZToF1bVhe2khoCfQgMRU8Su/VvmilsMDgciE6Z5o953oFyQy3V0t59biQ0wnELRYt19mqHbDc0fmROnJ2RNQx9b3krdK19WmC+RSluPG3fsNwbm1NZ3FrKLmXg9I5Av6XJZTRsU0wx7yALu5gd+Dik1WAgNAFTvO8p1Ei5GkB4SKoDF5nH+qvJGIUqpFUuc5FtpzQSqcQN1y7fok6n9kJ2cvHyMe+gV58o72S5LzO07CCi8ujQb2VST8deT+YwiojUymJ382wIDAQAB";
        config.merchantPrivateKey = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCoMicDKP6g+QQ5hcMofaVfJnGGbtoiXPNEIzZutaXcMje002WBYORvypBw1uWIO66oaLeTXZu2hlggRJFQZVdnQGT5P3u2BHmXXNpuk+dSDfgp7DsQpRmtkgSxjKv7HMFazY5wC4Z/IFFrJ6nAieeVeMwvUPHLTUz0+P1tqsMbqPpyfAp6vHPqxDZPMOMtkKJQj6Bj90h/Fje+75n/Snl6MwAynSV4e/Fi65QESYupvk5Ajfii9R1pjSuECnK2ua2oVJi7ZHEj31mXZlzqkgSlot/9jvqzus4oawvb2XAA8cH3R4A2UJ6cZH+CuoA4wJ/Bf4dpx2ouqNdBS3A+JUcTAgMBAAECggEAPFQ44A5NPsny9PsApD5lGbr9T13lihvS+eazeIRZKKEukAKfTwfFCEAdDQxXqkTWAfwFlKsn1hFb/L77bF3dOuDDFw/pPMFgJ6Wbh7wvZaQzFj5wdSjzC+nrGxA9A8YtqHdAlvnbVS/vZEMGxVPxqBB7FKGxCN4CM4A3zqVIdFvSW+QJm1xQ8WB7KXJzjhv7J1R4h02FcC6KGAh2meThVkNeZRcv978biq1S+Fm8WDXBZGoBXqsgqcHMkNezjpgCqMLDn0GUHmy+ygA/EeFS9vCB7Nnj0OiT6j7qoZVp/d+ffGvzo7mDxOBlNoOBtIglw6RHpQGpjBQ8XHeOM8MdyQKBgQDxlkVXvbdiRi/6NRZaNPPL9Nkr+JO0Cok5nSgRJR1TE8iVFwQoGG8WMEwcN73DSo/3UAOk75QhKEYw+1PF6PPPwruDCVDow6IBZiEFIVmRNS+AeD3BPQvxgEKo24ZYKSOWeNur5XGd03ff25IZ+1Ob3rlciHmnJZXhbF3fMtnE1wKBgQCyOvz+3l3xI3KAXRVqoqYq4s0Nx7pYqNMlPnKdjQumEPvihAerAEpUSkc87d+vehygE9e0lcMzuI3ynBh0kA0N7QgQepk60I2zxbphiBHsJ/siTVT2p0K+hm6evYtYtrxIs4ooi6qjA7Vw7ysCu/OpGFfmPCXdSLhCrW308CRMJQKBgQDvqara80tUw9petoGZNcTSBfHYDD7DMlyV+lFASPiBbTPkq7Ok7zBs67blm2q2rWaK6ybiXgYuqpyPhRTDVmlhJ4cL5JcOgt1Z0+5X/5CyW/fm0+I3rI8Horz+dOEe3pj+cqFCM1l2P85BFrsm6Uw1akuq/3zoE4JP4khx3yloewKBgQCTSDW7M+yzkVRUiRdimGfydpt2sL7n9xMM6u7yDRU8NTrBM9PWVpYV04/M5NF9A87V5a/R9hRqEC+U9bMr18JB6MdKPtWTCbafgV0ErdoJqfxYm0kDzrJUGWkeWonlVJSBC4S2/ruSnET6v1lul/zBlnFEvB2aIDbMOt7rkuqb1QKBgDNM0lAYHEjcqGVI4NGAwpNBn2Kke/cGQMO1WLjBvF2QdWnSZceRnH4S4kZb3lkdTX/8aGcpFfpruvvd7kuhD6hrtMMOQCmsxLFl0RzyAFmT1W2vmBUKXZldpUbQU9PR/RAyLR2q0+5LFu7z/rz0sUGZBd2NDf2GH5qeoL+1dphB";
        return config;
    }
}
