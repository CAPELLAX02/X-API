# X API: A Reverse Engineering Backend Project for X, FNA Twitter

---

TODO

---

## Generating and Managing RSA Keys for Authentication

In this project, RSA keys are used for authentication and encryption purposes. This section explains how to generate, convert, and verify the RSA key pair required for secure communication.

### Why Do We Need RSA Keys?
- **Public-Key Cryptography:** RSA (Rivest-Shamir-Adleman) is an asymmetric encryption algorithm that uses a public-private key pair for security.
- **Secure Token Signing:** The private key is used to sign JSON Web Tokens (JWT), and the public key is used to verify them.
- **Encryption & Decryption:** The keys allows secure communication between clients and servers.

### Generating RSA Key Pair
1. **Navigate to `backend/src/main/resources` directory and create the `certs` directory within the `resources`:**
```
cd backend/src/main/resources
```
```
mkdir certs
```
Run the following commands inside the `backend/src/main/resources/certs` directory to generate the required keys:
2. **Generate a 2048-bit RSA Private Key**
```
openssl genrsa -out keypair.pem 2048
```
- This command created a 2048-bit RSA private key in a file named `keypair.pem`. The private key is the most sensitive part and should never be exposed publicly in production environment.
3. **Extract the Public Key**
```
openssl rsa -in keypair.pem -pubout -out public.pem
```
* This extracts the public key from `keypair.pem` and saves it to `public.pem`. The public key can be freely shared because it is used to verify signatures, not to sign them.
4. **Convert the Private Key to PKCS8 Format**
```
openssl pkcs8 -topk8 -inform PEM -outform PEM -nocrypt -in keypair.pem -out private.pem
```
**Why convert to PKCS8?**
- Spring Security expects the RSA private key in PKCS8 format for better compatibility.
- This command removes any passphrase encryption, making it ready for use in Java applications.

5. **Ensure the correct paths are referenced in `backend/src/main/resources/application.yml`.**
```yaml
rsa:
    public-key: classpath:certs/public.pem
    private-key: classpath:certs/private.pem
```
6. **Verify the Keys**
   - Before running the application, confirm that the keys are correctly formatted and working.
     - Check the private key with the following command:
       - ```
         openssl rsa -in src/main/resources/certs/private.pem -check
         ```
         - If everything is correct, you should see something as follows:
           - ```
             RSA key ok
             writing RSA key
             -----BEGIN PRIVATE KEY-----
             MIIEvAIBADANBgkqhkiG9w0BAQEFAASCBKYwggSiAgEAAoIBAQC9bd2zhxB3NWy3
             0iTwBZ4qY/xiMbCWUYgwg4MlrVuCRiYtx9v+lwypZakb3z5Q0/1stPfy0l0MYXKC
             3gcTfA7frZaFPWNcDwa+g9RdnOI7U5yGbPk9W7DdzDvsJGwq6DfFC4MKndYp1deF
             To2dY9o6Cya0xGwf+tBqu3g122VwgYLV2nKo4a+J/kacPt/VMNqGs7EEJw4CEjED
             t7aSZKHp+8LTlGwec9rDgpYSxTTMAm5K0xGyTFxAoJ5EqkLbik/Adbp275AKfZtb
             I+QT33/Poha6yzROhdbpHigTQztdUQxqjE8BAfO27mEUW63JigztEmoK280I+ipr
             Kr0hVM99AgMBAAECggEAFKJ6KwtEdOfh7ug6U9cY0p9Pp9an2MgK4NYCnEEVHczM
             beI+ia4kl/NWB446+I0PJx9TfyUBM88NrUnIVYwHBsCfC+aTAz0zy98KbCQh1LqX
             62DFibSKQK71vBR/n/Dp2lIVtTtaT/ZWYtQawxFUfUwnrDtVV+p3d5jyUFOU6jtR
             YKOaUFwG+msvxhMKIVut6wqbqPydMPHkR67PohLv5hFHYImZ2iuIUQQXs3RLFptL
             5Q1DN1ieDsqGq8XISsafLszQ26qsKCIl8qFGwd3fUthcGh+fCb3zsUUa9XPB91gf
             BcskLU+EU2VJ8c3YveuZIQKy0neq99fWJHHBIBHqNQKBgQDmzvbBXAvtth/ZUQGZ
             7zzkGqWxEdauozMkjQvOJCSu7PkI5u7DUtJIDhA5fTgjuQ0T62QZDKYL0zH360o3
             Mn7T9ENtuxOlmFovurvkZnuBk89SpVqFbRawmGJld5uP9yFvR3NCtli6QeQK4nb3
             2oa0xUel8U7PWrBmeextufnIEwKBgQDSGrhpeaWfK+XOm1sZxufVDApRJYwSbqV8
             gONe/GA5PJU7T8Nfb3Vy2JMA+tFuWQy3IOOct7qPau/e0SES/dHJj7zWM4ngO58G
             VPM6DRzOMThkrjYC7ANXQWST71xivpOBPdfSJbyjLHmPbFDXyxHdmidcY3mlqeJr
             UuRj6+0cLwKBgDobMo39UVd3+U6pkSCi1Q6MEtigKA7xMSfrhmRQPxf2ur9d/BAa
             YqwXNkhy2NZNeRhhs2KHB6qEcdj7WETLJpjPrsYKSejvkQzhEvpuPnuhrZkd3csn
             aoXs5yCqWvSEf9tW/pJ1+6JwgiunVdISJsWNwPrq5C9Zon06BqadImAXAoGALZMu
             PXLtwkE7cD2y3TyQPV6HPmLup3Do5MDwkUIdfUzu8hazQphi2a6w9J1zoIQghfjU
             ZsJT7Zg8wBCNJBl60EYlqDE1zzYfoLf8qrL9dJyopu5DOQ8JCnxc3NonKCQ+yuIn
             VqZo7NxQ4hOU1bpu/ararufFE3JuculgMWyT2nECgYAa6UTL87xTzRgLeUONaKPd
             7H7AKW9m53QcZv7XVD+1Aqxgtta6mmmXBXK3eIN5dRjTiaNmz5yDL2VB97x9SlNj
             6TrbgEuMNIijObwfA5KPQQ8fIlvkhezYOfEIpjFLFROnz6gFOliMCwvdH4PaL+4I
             Z3HpPna9Y0QzBsulA1S9iQ==
             -----END PRIVATE KEY-----
             ```
     - Secondly, check the public key with the following command:
       - ```
         openssl rsa -in src/main/resources/certs/public.pem -pubin -text -noout
         ```
         - If everything is correct, you should see something as follows:
           - ```
               Public-Key: (2048 bit)
               Modulus:
               00:bd:6d:dd:b3:87:10:77:35:6c:b7:d2:24:f0:05:
               9e:2a:63:fc:62:31:b0:96:51:88:30:83:83:25:ad:
               5b:82:46:26:2d:c7:db:fe:97:0c:a9:65:a9:1b:df:
               3e:50:d3:fd:6c:b4:f7:f2:d2:5d:0c:61:72:82:de:
               07:13:7c:0e:df:ad:96:85:3d:63:5c:0f:06:be:83:
               d4:5d:9c:e2:3b:53:9c:86:6c:f9:3d:5b:b0:dd:cc:
               3b:ec:24:6c:2a:e8:37:c5:0b:83:0a:9d:d6:29:d5:
               d7:85:4e:8d:9d:63:da:3a:0b:26:b4:c4:6c:1f:fa:
               d0:6a:bb:78:35:db:65:70:81:82:d5:da:72:a8:e1:
               af:89:fe:46:9c:3e:df:d5:30:da:86:b3:b1:04:27:
               0e:02:12:31:03:b7:b6:92:64:a1:e9:fb:c2:d3:94:
               6c:1e:73:da:c3:82:96:12:c5:34:cc:02:6e:4a:d3:
               11:b2:4c:5c:40:a0:9e:44:aa:42:db:8a:4f:c0:75:
               ba:76:ef:90:0a:7d:9b:5b:23:e4:13:df:7f:cf:a2:
               16:ba:cb:34:4e:85:d6:e9:1e:28:13:43:3b:5d:51:
               0c:6a:8c:4f:01:01:f3:b6:ee:61:14:5b:ad:c9:8a:
               0c:ed:12:6a:0a:db:cd:08:fa:2a:6b:2a:bd:21:54:
               cf:7d
               Exponent: 65537 (0x10001)
               ```
---
















