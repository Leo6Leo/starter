package com.example.starter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import io.vertx.core.AbstractVerticle;
import io.vertx.core.Promise;
import io.vertx.core.http.HttpServer;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.core.net.PemKeyCertOptions;
import io.vertx.core.net.SSLOptions;

public class MainVerticle extends AbstractVerticle {

  @Override
  public void start(Promise<Void> startPromise) throws Exception {

    File tlsKeyFile = new File("secret_volume/tls.key");
    File tlsCrtFile = new File("secret_volume/tls.crt");

    PemKeyCertOptions keyCertOptions = new PemKeyCertOptions().setKeyPath(tlsKeyFile.getPath())
        .setCertPath(tlsCrtFile.getPath());

    HttpServerOptions options = new HttpServerOptions()
        .setSsl(true)
        .setPemKeyCertOptions(keyCertOptions);

   HttpServer server = vertx.createHttpServer(options);
   server.requestHandler(req -> {
      req.response()
          .putHeader("content-type", "text/plain")
          .end("Hello from Vert.x over HTTPS!");
          updateSSLOptions(server);
    }).listen(8443, http -> {
      if (http.succeeded()) {
        startPromise.complete();
        System.out.println("HTTPS server started on port 8443");
        
      } else {
        startPromise.fail(http.cause());
      }
      ;
    });
  }

  public HttpServerOptions updateSSLOptions(HttpServer httpsServer) {
    File tlsKeyFile = new File("secret_volume/tls.key");
    File tlsCrtFile = new File("secret_volume/tls.crt");

    // overwrite the tls.key and tls.crt files with the new ones
    // These are the wrong certificate pair, which will fail the TLS handshake
     String new_TLS_Cert = "-----BEGIN CERTIFICATE-----\nMIIDmDCCAoCgAwIBAgIUZx4ztTK7wyEpRYKkKqM9+oFr+PwwDQYJKoZIhvcNAQEL\nBQAwJzELMAkGA1UEBhMCVVMxGDAWBgNVBAMMD0V4YW1wbGUtUm9vdC1DQTAeFw0y\n  MzA3MTcxNDI1MzhaFw0yNjA1MDYxNDI1MzhaMG0xCzAJBgNVBAYTAlVTMRIwEAYD\nVQQIDAlZb3VyU3RhdGUxETAPBgNVBAcMCFlvdXJDaXR5MR0wGwYDVQQKDBRFeGFt\n  cGxlLUNlcnRpZmljYXRlczEYMBYGA1UEAwwPbG9jYWxob3N0LmxvY2FsMIIBIjAN\nBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAyq0tbWj3zb/lhcykAAXlc8RVVPiZ\n898NxNV1od3XvFUFRYkQP9DU/3nE/5DxDQbQmfTlov50WbgSgQxt9GR7iC3lheOm\nB3ODaA0p3C7bBg7LeUvtrhvPyHITDI9Aqy8cUO5XHVgbTceW7XOvcmju/DVpm9Id\niSpEEPMT2GsuLQ2rVvNupIccYRe0NhZly7l27AAkf5y1G2Yd9Oklt+gOPNPB+afH\n  /eFlYRrKokp58Kt1eyDNAwaYV8arEKIapU2AQheZTZQSBOi/tFCc7oKFQOmO9sFf\nHEuQfCVd8TZJ2vb7qdiLVlgTDwjVYmUkfkxR7JJ/feDacyfjGkqYd1bngQIDAQAB\no3YwdDAfBgNVHSMEGDAWgBQGanp895VYiwZNv+X+JJ7GWjQtWTAJBgNVHRMEAjAA\nMAsGA1UdDwQEAwIE8DAaBgNVHREEEzARgglsb2NhbGhvc3SHBH8AAAEwHQYDVR0O\nBBYEFOlfLUC1MJOOjGRWfVzHQYA+Iya4MA0GCSqGSIb3DQEBCwUAA4IBAQACCgdN\nSj+W39W+8JdHpBU/fw1wwNDB4SyIyxAgPXp8TWiOwoo3ozcALP44ab4jP9b+Etlm\nyNMNdayOf42SCZUhihO4PKiiqDgolDQfYaZbiIEXJ/xaXtao5SxyBPY77eXtXN/+\n  E7/TOWQ5U7qJYd7H5vqhlFk6fn7s6WKkue8ELUrWh8r3THASXUsa8xzxHu0nsp2v\nSsbYyR0vyrGE4yvComvl75Igw6jY70cswWdyThGKV6ZLip2BrjLQlFhr3IZN5tbg\nrHxaoqIen8NYjNpBdJDInPMFZshZSx1lAzw6uwP4OuM5WQHgYEk7V+TkOU3osqgD\n5bOo/SpCokC166Ym\n-----END CERTIFICATE-----";
        String new_TLS_key = "-----BEGIN PRIVATE KEY-----\nMIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQDKrS1taPfNv+WF\nzKQABeVzxFVU+Jnz3w3E1XWh3de8VQVFiRA/0NT/ecT/kPENBtCZ9OWi/nRZuBKB\nDG30ZHuILeWF46YHc4NoDSncLtsGDst5S+2uG8/IchMMj0CrLxxQ7lcdWBtNx5bt\nc69yaO78NWmb0h2JKkQQ8xPYay4tDatW826khxxhF7Q2FmXLuXbsACR/nLUbZh30\n6SW36A4808H5p8f94WVhGsqiSnnwq3V7IM0DBphXxqsQohqlTYBCF5lNlBIE6L+0\nUJzugoVA6Y72wV8cS5B8JV3xNkna9vup2ItWWBMPCNViZSR+TFHskn994NpzJ+Ma\nSph3VueBAgMBAAECggEABqD+ZN8zh6u4lpU4YfXPaOdpgRN2eZb4jNEMfWRTm4nO\nV9VhTi0G4mo5qsAzWiE4bminoBqhdJPEKytcZ0toDO6vXJ8y/XhmOl9/2H9B06Nl\nPUzh87leJOiyPc1rqI2sZ+s7ty58CiG2ioKnoN7UvjQDBcEsDSHwQvuoUQJEat3C\nIxK792JWumgDzJWy2YOVGbcYKSLRV5q+IEdIH7SG5NDTMTs+x+os0azGLe/X+++o\nbhbXlYhMnA3X1XQ9yiJaK4QfwEc+i3YFNjRuD/cS8dv/dPji9F6eS7HewscW+f5f\nEoUMSSvO6FqYQt1S7jD9kDzQ8TaKlA/pRrVumTs96QKBgQDR2spitSuPm0K+wLMj\n5gj8OyM4eW0pePQQHYNy4UdHu9EZ/w88WNQCKF1RKbAcBECxns97oI6WskAumzpm\n1jqa6Ofe2y8k1Vc6t3PTqVU98ms59M4ifb0aq+entzp3FIQAOaO/2x5+NaUrA/kp\n6EX4IG5UNFv1+J6dpzI+Il/8bQKBgQD3Pk9/pjrAsigO4qrN54x1CsVorrSIxwp6\n4RUI486gAZx7bKjn8hcnKnT/U+9Z8ui4i318kuly4nDSpvW8e4PXVLt9fTQKa/4f\nBtFoizTu0PqAmntljbVBZ9a1QN5puc7BxCbYO/md6BQ68dlXnu6NuPys/E8dIuAO\nndOVbj5C5QKBgQClWVUaDVHzZwxiLId6A6iUxSvtNY/Tm6ACip6mB+cYGF6bsyKY\nFA2IXbGZX9WJXbhzu4QUDuAK0QxNLLYJjUbEBDuelulAhnCirSWwYr3tf3MJSWCa\nQKSdvVFcDr0cUqfnXYMuikIug6pOiGTspj1rUnJcGp1S48Bmy/SEjKVAyQKBgEcA\n8QnCrlrKjzB/LfhGCBNQzZKboaMqLjtNyqGr8poG/G6BrRw3bSjFS6ZL75AQb38Y\nKCiPdFWW7DnC0w2XFyzO261VOI3Jp8g3SApS+Behkl8+fjOS97vZ21JgV79bKiKB\nd3pf9va/QJgQ/o7oSLAQsRfoubuvWVM5RhtC9sR1AoGBAMUpJh9LOJvaCFTJ51SZ\nERr1RMt6Q13ssG7ytGQIsJ7PSoRJMBN1ZrEYANAuJU5n3V8AxWcKIjV3hoXc2yg6\nTfwO4tuRZ0hSBe+POhImNGNVJ71yylGDFJc/21KMRxXi1IIKVr0qjhv0IQcvDDN2\nQCDcINom+skQGHJlbPdrpwNW\n-----END PRIVATE KEY-----";

        FileWriter tlsCrtWriter;
        FileWriter tlsKeyWriter;
        try {
            tlsCrtWriter = new FileWriter(tlsCrtFile);
            tlsCrtWriter.write(new_TLS_Cert);
            tlsCrtWriter.close();
            tlsKeyWriter = new FileWriter(tlsKeyFile);
            tlsKeyWriter.write(new_TLS_key);
            tlsKeyWriter.close();
        } catch (IOException var11) {
            System.out.println("An error occurred.");
            var11.printStackTrace();
        }

    PemKeyCertOptions keyCertOptions = new PemKeyCertOptions().setKeyPath(tlsKeyFile.getPath())
        .setCertPath(tlsCrtFile.getPath());

    HttpServerOptions options = new HttpServerOptions()
        .setSsl(true)
        .setPemKeyCertOptions(keyCertOptions);

    httpsServer.updateSSLOptions(new SSLOptions().setKeyCertOptions(keyCertOptions)).onComplete(res -> {
      if (res.succeeded()) {
        System.out.println("TLS certificate updated");
      } else {
        System.out.println("TLS certificate update failed");
      }
    });

    return options;
  }



}
