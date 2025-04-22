package Service.AIservice;

public class AIAnalyzerThread implements Runnable {
    private final String userRequest;
    private final String filePath;
    private final String startTimeStr;
    private final String endTimeStr;

    public AIAnalyzerThread(String userRequest, String filePath,String startTimeStr, String endTimeStr) {
        this.userRequest = userRequest;
        this.filePath = filePath;
        this.startTimeStr = startTimeStr;
        this.endTimeStr = endTimeStr;
    }

    @Override
    public void run() {
        String result = new AITransactionService().analyzeTransactions(userRequest, filePath, startTimeStr, endTimeStr);
        System.out.println("AI分析结果: " + result);
        // TODO: 如果是UI程序，可用 SwingUtilities.invokeLater() 更新UI组件
    }

}
