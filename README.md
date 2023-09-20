# Vert.x HTTPS Server SSLOptions Bug Reproduction

This repository demonstrates an issue with updating `SSLOptions` in a Vert.x HTTPS server, as discussed in issue #<ISSUE_NUMBER>. 

## Problem Description

When updating the `SSLOptions` of a Vert.x HTTPS server using the path of secret files, the server doesn't reflect the new values. However, when the actual value of the secret files is passed, the server updates correctly.

## Repository Structure

- `MainVerticle.java`: Contains the implementation of the Vert.x HTTPS server.
- `secret_volume`: A directory storing symbolic links to the actual secret files (`tls.crt` and `tls.key`) at the root.

## Steps to Reproduce

### 1. Generate the Secrets

Run the following OpenSSL command:

```
openssl req -x509 -nodes -days 365 -newkey rsa:2048 -keyout tls.key -out tls.crt -config san.cnf
```

### 2. Add Certificate to Local Trusted CA List

For Fedora:

```
sudo cp tls.crt /etc/pki/ca-trust/source/anchors/
sudo update-ca-trust extract
```

*Note:* Adjust this step according to your OS if you're not using Fedora.

### 3. Compile the Java Application

Execute:

```
mvn clean package
```

### 4. Start the Vert.x Server

Run:

```
mvn exec:java
```

Wait for the message that indicates the server is running:

```
HTTPS server started on port 8443
Sep 20, 2023 5:01:54 PM io.vertx.core.impl.launcher.commands.VertxIsolatedDeployer
INFO: Succeeded in deploying verticle
```

### 5. Send Request to HTTPS Server

Open another terminal and execute:

```
http https://localhost:8443
```

On the first request, you should see:

```
HTTP/1.1 200 OK
content-length: 29
content-type: text/plain

Hello from Vert.x over HTTPS!
```

In the terminal where the server is running, you'll notice `TLS certificate updated`.

### 6. Send Another Request

Execute again:

```
http https://localhost:8443
```

This time, due to the updated (and incorrect) `SSLOptions`, you should encounter an error:

```
http: error: SSLError: HTTPSConnectionPool(host='localhost', port=8443): Max retries exceeded with url: / (Caused by SSLError(SSLCertVerificationError(1, '[SSL: CERTIFICATE_VERIFY_FAILED] certificate verify failed: unable to get local issuer certificate (_ssl.c:1002)'))) while doing a GET request to URL: https://localhost:8443/
```

However, as explained earlier, the issue is that you still receive the `OK` message, indicating that the `SSLOptions` update didn't take effect.

---

## Conclusion

This repository showcases the bug where updating `SSLOptions` with secret file paths doesn't work as expected in Vert.x HTTPS servers. Your contribution towards resolving this issue would be greatly appreciated!
