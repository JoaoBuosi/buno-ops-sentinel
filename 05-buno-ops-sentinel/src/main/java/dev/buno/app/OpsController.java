package dev.buno.app;
import org.springframework.web.bind.annotation.*;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.CopyOnWriteArrayList;
@RestController @RequestMapping("/api/ops")
public class OpsController {
 private final List<Map<String,Object>> events=new CopyOnWriteArrayList<>();
 private final AegisRuntimeClient aegis;
 public OpsController(AegisRuntimeClient a){aegis=a;}
 public record Event(String source,String severity,String message){}
 public record Analyze(String question){}
 @PostMapping("/events") public Map<String,Object> ingest(@RequestBody Event e){
  if(e.source()==null||e.severity()==null||e.message()==null)throw new IllegalArgumentException("source, severity e message são obrigatórios");
  Map<String,Object> item=Map.of("id",UUID.randomUUID().toString(),"source",e.source(),"severity",e.severity().toUpperCase(Locale.ROOT),"message",e.message(),"receivedAt",Instant.now().toString());
  events.add(item);return item;
 }
 @GetMapping("/events") public List<Map<String,Object>> list(@RequestParam(defaultValue="100") int limit){
  int n=Math.max(1,Math.min(limit,500)); return events.stream().skip(Math.max(0,events.size()-n)).toList();
 }
 @PostMapping("/analyze") public Map<String,Object> analyze(@RequestBody Analyze a){
  String context=events.stream().skip(Math.max(0,events.size()-100)).map(Object::toString).reduce("",(x,y)->x+"\n"+y);
  String result=aegis.complete("Você é o Buno Ops Sentinel. Analise eventos operacionais sem inventar fatos. Sugira diagnóstico e próximos passos seguros; não execute ações.", "Eventos recentes:\n"+context+"\nPergunta: "+a.question());
  return Map.of("analysis",result,"eventCount",events.size(),"generatedAt",Instant.now().toString());
 }
}
