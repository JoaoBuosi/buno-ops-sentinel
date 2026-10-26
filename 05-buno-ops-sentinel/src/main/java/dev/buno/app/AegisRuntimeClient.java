package dev.buno.app;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import java.util.Map;
@Service
public class AegisRuntimeClient {
 private final RestClient client;
 private final String chatPath;
 public AegisRuntimeClient(@Value("${aegis.base-url:http://host.docker.internal:8099}") String baseUrl,
                           @Value("${aegis.api-key:}") String apiKey,
                           @Value("${aegis.chat-path:/v1/chat/completions}") String chatPath) {
  this.chatPath=chatPath;
  this.client=RestClient.builder().baseUrl(baseUrl)
   .defaultHeaders(h->{if(apiKey!=null&&!apiKey.isBlank())h.setBearerAuth(apiKey);})
   .build();
 }
 public String complete(String system,String prompt) {
  Map<String,Object> body=Map.of("system",system,"prompt",prompt);
  try {
   Map<?,?> result=client.post().uri(chatPath).contentType(MediaType.APPLICATION_JSON)
    .body(body).retrieve().body(Map.class);
   if(result==null)return "Runtime Aegis retornou resposta vazia.";
   Object answer=result.get("answer");
   if(answer==null)answer=result.get("content");
   if(answer==null)answer=result.get("message");
   return answer==null?result.toString():String.valueOf(answer);
  } catch(Exception e) {
   throw new IllegalStateException("Falha ao chamar Buno Aegis Runtime. Confira URL, rota e contrato JSON configurados.",e);
  }
 }
}
