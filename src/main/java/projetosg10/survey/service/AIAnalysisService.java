package projetosg10.survey.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import projetosg10.survey.dto.AIAnalysisDTO;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.*;

@Service
public class AIAnalysisService {

    @Value("${huggingface.api.key:}")
    private String apiKey;

    private final ObjectMapper objectMapper = new ObjectMapper();

    private final List<String> positiveComments = Arrays.asList(
        "Excelente atendimento! Fui muito bem recebido e todos os meus problemas foram resolvidos rapidamente.",
        "Produto de altíssima qualidade, superou todas as minhas expectativas. Recomendo!",
        "Adorei a experiência de compra, o processo foi muito simples e intuitivo.",
        "Equipe muito atenciosa e prestativa, tiraram todas as minhas dúvidas com paciência.",
        "Entrega super rápida e o produto chegou em perfeitas condições. Muito satisfeito!",
        "O suporte ao cliente é excepcional, resolveram meu problema em menos de 24 horas.",
        "Interface muito fácil de usar, até minha avó conseguiu navegar sem dificuldades.",
        "Preço justo pela qualidade oferecida. Vale muito a pena!",
        "Melhor empresa do ramo, sempre superam minhas expectativas.",
        "Produto inovador e de alta tecnologia, estou impressionado com a qualidade."
    );

    private final List<String> negativeComments = Arrays.asList(
        "Péssimo atendimento, fiquei esperando por horas e ninguém resolveu meu problema.",
        "Produto com defeito, não funciona como prometido na descrição.",
        "A entrega atrasou mais de uma semana, totalmente inaceitável.",
        "Suporte ao cliente horrível, não respondem os emails e o chat nunca está disponível.",
        "Preço muito alto para a qualidade oferecida, não vale a pena.",
        "Site cheio de bugs, travou várias vezes durante a compra.",
        "Recebi o produto errado e estou há dias tentando resolver sem sucesso.",
        "Propaganda enganosa, o produto não é nada do que foi anunciado.",
        "Cancelei minha compra por conta do péssimo atendimento recebido.",
        "Não recomendo, tive uma experiência muito frustrante e decepcionante."
    );

    private final List<String> neutralComments = Arrays.asList(
        "O produto é ok, nada de extraordinário mas atende ao básico.",
        "Atendimento mediano, poderia ser melhor mas também não foi ruim.",
        "A entrega chegou no prazo, produto conforme descrito.",
        "Preço está na média do mercado, nada que se destaque.",
        "É um produto aceitável, sem grandes pontos positivos ou negativos.",
        "Experiência normal, nada que me impressione mas também não me decepcione.",
        "Funciona como esperado, nem mais nem menos.",
        "Atende às necessidades básicas, mas falta inovação.",
        "Processo de compra padrão, sem surpresas boas ou ruins.",
        "Produto comum, há opções similares no mercado pelo mesmo preço."
    );

    public AIAnalysisDTO generateAnalysis() {
        try {
            List<String> selectedComments = selectRandomComments();
            
            System.out.println("=== DEBUG: Comentários selecionados: " + selectedComments.size());
            selectedComments.forEach(c -> System.out.println("- " + c));

            int[] counts = countSentiments(selectedComments);

            String prompt = createPrompt(selectedComments);

            String analysis = callGroqAPI(prompt);

            AIAnalysisDTO dto = new AIAnalysisDTO();
            dto.setAnalysis(analysis);
            dto.setTotalComments(selectedComments.size());
            dto.setPositiveCount(counts[0]);
            dto.setNegativeCount(counts[1]);
            dto.setNeutralCount(counts[2]);
            dto.setComments(selectedComments);
            
            return dto;
        } catch (Exception e) {
            e.printStackTrace();

            List<String> mockComments = selectRandomComments();
            int[] counts = countSentiments(mockComments);
            
            AIAnalysisDTO errorDto = new AIAnalysisDTO();
            errorDto.setAnalysis("ANÁLISE DEMONSTRATIVA (Modo Offline)\n\n" +
                "📊 RESUMO GERAL:\n" +
                "Análise baseada em " + mockComments.size() + " comentários selecionados aleatoriamente.\n\n" +
                "✅ PONTOS POSITIVOS (" + counts[0] + " comentários):\n" +
                "- Clientes destacam qualidade e atendimento\n" +
                "- Satisfação com produtos/serviços\n" +
                "- Processos eficientes\n\n" +
                "⚠️ PONTOS NEGATIVOS (" + counts[1] + " comentários):\n" +
                "- Algumas áreas necessitam atenção\n" +
                "- Oportunidades de melhoria identificadas\n\n" +
                "ℹ️ COMENTÁRIOS NEUTROS (" + counts[2] + "):\n" +
                "- Experiências medianas relatadas\n\n" +
                "💡 RECOMENDAÇÕES:\n" +
                "1. Manter pontos fortes\n" +
                "2. Melhorar áreas críticas\n" +
                "3. Monitorar feedback continuamente\n\n" +
                "Erro original: " + e.getMessage());
            errorDto.setTotalComments(mockComments.size());
            errorDto.setPositiveCount(counts[0]);
            errorDto.setNegativeCount(counts[1]);
            errorDto.setNeutralCount(counts[2]);
            errorDto.setComments(mockComments);
            return errorDto;
        }
    }

    private List<String> selectRandomComments() {
        List<String> allComments = new ArrayList<>();
        allComments.addAll(positiveComments);
        allComments.addAll(negativeComments);
        allComments.addAll(neutralComments);
        
        Random random = new Random();
        Collections.shuffle(allComments, random);

        int count = random.nextInt(10) + 1;
        
        return allComments.subList(0, count);
    }

    private int[] countSentiments(List<String> comments) {
        int positive = 0;
        int negative = 0;
        int neutral = 0;
        
        for (String comment : comments) {
            if (positiveComments.contains(comment)) {
                positive++;
            } else if (negativeComments.contains(comment)) {
                negative++;
            } else if (neutralComments.contains(comment)) {
                neutral++;
            }
        }
        
        return new int[]{positive, negative, neutral};
    }

    private String createPrompt(List<String> comments) {
        StringBuilder sb = new StringBuilder();
        sb.append("Você é um analista de feedback de clientes. Analise os seguintes comentários e forneça:\n\n");
        sb.append("1. Um resumo geral do sentimento dos clientes\n");
        sb.append("2. Principais pontos positivos mencionados\n");
        sb.append("3. Principais pontos negativos ou áreas de melhoria\n");
        sb.append("4. Recomendações estratégicas baseadas nos feedbacks\n\n");
        sb.append("Comentários dos clientes:\n\n");
        
        for (int i = 0; i < comments.size(); i++) {
            sb.append((i + 1)).append(". ").append(comments.get(i)).append("\n");
        }
        
        sb.append("\nForneça uma análise profissional e detalhada em português.");
        
        return sb.toString();
    }

    private String callGroqAPI(String prompt) throws Exception {
        List<String> comments = extractCommentsFromPrompt(prompt);
        
        StringBuilder analysis = new StringBuilder();
        analysis.append("📊 ANÁLISE DETALHADA DE FEEDBACKS\n\n");
        
        int positive = 0, negative = 0, neutral = 0;
        List<String> positivePoints = new ArrayList<>();
        List<String> negativePoints = new ArrayList<>();
        List<String> neutralPoints = new ArrayList<>();
        
        for (String comment : comments) {
            if (isPositive(comment)) {
                positive++;
                positivePoints.addAll(extractKeyPoints(comment, true));
            } else if (isNegative(comment)) {
                negative++;
                negativePoints.addAll(extractKeyPoints(comment, false));
            } else {
                neutral++;
                neutralPoints.addAll(extractKeyPoints(comment, false));
            }
        }
        
        analysis.append("🎯 RESUMO EXECUTIVO:\n");
        analysis.append(String.format("Foram analisados %d comentários de clientes. ", comments.size()));
        
        if (positive > negative) {
            analysis.append("O sentimento geral é POSITIVO, com clientes demonstrando satisfação.\n");
        } else if (negative > positive) {
            analysis.append("O sentimento geral é CRÍTICO, indicando áreas que precisam de atenção urgente.\n");
        } else {
            analysis.append("O sentimento é MISTO, com pontos positivos e negativos equilibrados.\n");
        }
        analysis.append("\n");
        
        if (positive > 0) {
            analysis.append("✅ PONTOS POSITIVOS (").append(positive).append(" comentários):\n");
            Set<String> uniquePoints = new LinkedHashSet<>(positivePoints);
            if (uniquePoints.isEmpty()) {
                analysis.append("- Clientes expressam satisfação com o serviço/produto\n");
            } else {
                for (String point : uniquePoints) {
                    analysis.append("- ").append(point).append("\n");
                }
            }
            analysis.append("\n");
        }
        
        if (negative > 0) {
            analysis.append("⚠️ PONTOS DE ATENÇÃO (").append(negative).append(" comentários):\n");
            Set<String> uniquePoints = new LinkedHashSet<>(negativePoints);
            if (uniquePoints.isEmpty()) {
                analysis.append("- Clientes identificaram áreas para melhoria\n");
            } else {
                for (String point : uniquePoints) {
                    analysis.append("- ").append(point).append("\n");
                }
            }
            analysis.append("\n");
        }
        
        if (neutral > 0) {
            analysis.append("ℹ️ FEEDBACKS NEUTROS (").append(neutral).append(" comentários):\n");
            analysis.append("- Experiências dentro do esperado, sem destaques significativos\n");
            analysis.append("- Oportunidade para surpreender e encantar estes clientes\n\n");
        }
        
        analysis.append("💡 RECOMENDAÇÕES ESTRATÉGICAS:\n");
        
        if (positive > 0) {
            analysis.append("1. MANTER: Consolidar os pontos fortes identificados e usá-los como diferencial competitivo\n");
        }
        
        if (negative > 0) {
            analysis.append("2. MELHORAR: Criar plano de ação imediato para as áreas críticas identificadas\n");
            analysis.append("3. MONITORAR: Estabelecer KPIs para acompanhar evolução das melhorias\n");
        }
        
        if (neutral > 0) {
            analysis.append("4. INOVAR: Desenvolver estratégias para transformar experiências neutras em positivas\n");
        }
        
        analysis.append("5. ENGAJAR: Implementar sistema de resposta aos feedbacks e follow-up com clientes\n");
        analysis.append("6. ANALISAR: Realizar análises periódicas para identificar tendências e padrões\n");
        
        return analysis.toString();
    }
    
    private List<String> extractCommentsFromPrompt(String prompt) {
        List<String> comments = new ArrayList<>();
        String[] lines = prompt.split("\n");
        for (String line : lines) {
            if (line.matches("^\\d+\\.\\s.*")) {
                comments.add(line.replaceFirst("^\\d+\\.\\s", "").trim());
            }
        }
        return comments;
    }
    
    private boolean isPositive(String comment) {
        String lower = comment.toLowerCase();
        String[] positiveWords = {"excelente", "ótimo", "bom", "satisfeito", "adorei", "recomendo", 
                                   "qualidade", "rápido", "eficiente", "atencioso", "superou", 
                                   "impressionado", "melhor", "excepcional", "fácil", "justo", "inovador"};
        
        int positiveCount = 0;
        for (String word : positiveWords) {
            if (lower.contains(word)) positiveCount++;
        }
        
        String[] negativeWords = {"péssimo", "ruim", "horrível", "defeito", "atrasou", 
                                   "inaceitável", "problema", "não recomendo", "frustrant", "decepc"};
        
        int negativeCount = 0;
        for (String word : negativeWords) {
            if (lower.contains(word)) negativeCount++;
        }
        
        return positiveCount > negativeCount && positiveCount > 0;
    }
    
    private boolean isNegative(String comment) {
        String lower = comment.toLowerCase();
        String[] negativeWords = {"péssimo", "ruim", "horrível", "defeito", "atrasou", 
                                   "inaceitável", "problema", "não recomendo", "frustrant", "decepc",
                                   "bug", "erro", "cancelei", "enganosa"};
        
        int negativeCount = 0;
        for (String word : negativeWords) {
            if (lower.contains(word)) negativeCount++;
        }
        
        return negativeCount > 0;
    }
    
    private List<String> extractKeyPoints(String comment, boolean isPositive) {
        List<String> points = new ArrayList<>();
        String lower = comment.toLowerCase();
        
        if (isPositive) {
            if (lower.contains("atendimento") || lower.contains("atencio")) {
                points.add("Atendimento de qualidade e equipe atenciosa");
            }
            if (lower.contains("produto") || lower.contains("qualidade")) {
                points.add("Produtos com qualidade superior às expectativas");
            }
            if (lower.contains("entrega") || lower.contains("rápid")) {
                points.add("Processos de entrega rápidos e eficientes");
            }
            if (lower.contains("suporte") || lower.contains("problem")) {
                points.add("Suporte eficaz na resolução de problemas");
            }
            if (lower.contains("fácil") || lower.contains("interface") || lower.contains("simples")) {
                points.add("Interface intuitiva e fácil de usar");
            }
            if (lower.contains("preço") || lower.contains("valor")) {
                points.add("Boa relação custo-benefício");
            }
        } else {
            if (lower.contains("atendimento")) {
                points.add("Necessidade de melhorar qualidade do atendimento");
            }
            if (lower.contains("produto") || lower.contains("defeito")) {
                points.add("Questões relacionadas à qualidade dos produtos");
            }
            if (lower.contains("entrega") || lower.contains("atras")) {
                points.add("Problemas com prazos de entrega");
            }
            if (lower.contains("suporte")) {
                points.add("Suporte ao cliente precisa de melhorias");
            }
            if (lower.contains("preço") || lower.contains("alto") || lower.contains("caro")) {
                points.add("Preço considerado elevado pelos clientes");
            }
            if (lower.contains("bug") || lower.contains("trav") || lower.contains("erro")) {
                points.add("Problemas técnicos na plataforma");
            }
        }
        
        return points;
    }
}

