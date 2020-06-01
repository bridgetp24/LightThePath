public class RiskAssessment {

    private double risk;
    private double riskDueToPopulation;
    private double riskDueToDeltaPopulation;

    public RiskAssessment(double risk, double riskDueToPopulation, double riskDueToDeltaPopulation) {
        this.risk = risk;
        this.riskDueToPopulation = riskDueToPopulation;
        this.riskDueToDeltaPopulation = riskDueToDeltaPopulation;
    }

    public double getRisk() {
        return risk;
    }

    public double getRiskDueToDeltaPopulation() {
        return riskDueToDeltaPopulation;
    }

    public double getRiskDueToPopulation() {
        return riskDueToPopulation;
    }
}
