package gr.petalidis.datamars.inspections.utilities;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class WGS84ConverterTest {

    private final double [] alexandroupoli= {40.852446,25.873036};
    private final double [] herakleio = {35.327057,25.123312};
    private final double [] kerkyra = {39.623892,19.911384};
    private final double [] mytilhnh = {39.097146, 26.551932};
    private final double [] athens = {37.993224,23.722582};

    private final double [] alexandroupoliEGSA= {657732.40951566,4523780.0068051};
    private final double [] herakleioEGSA = {601948.21700142,3909603.4575964};
    private final double [] kerkyraEGSA = {148882.67195731,4393720.9466596};
    private final double [] mytilhnhEGSA = {720541.3393848, 4330370.5548326};
    private final double [] athensEGSA = {475492.25132546,4204811.9599447};
    @Test
    public void toGGRS87Alexandroupoli() {
        assertArrayEquals(alexandroupoliEGSA,WGS84Converter.toGGRS87(alexandroupoli[0],alexandroupoli[1]),0.01);
    }
    @Test
    public void toGGRS87Hrakleio() {
        assertArrayEquals(herakleioEGSA,WGS84Converter.toGGRS87(herakleio[0],herakleio[1]),0.01);
    } @Test
    public void toGGRS87Kerkyra() {
        assertArrayEquals(kerkyraEGSA,WGS84Converter.toGGRS87(kerkyra[0],kerkyra[1]),0.01);
    } @Test
    public void toGGRS87Mytilhnh() {
        assertArrayEquals(mytilhnhEGSA,WGS84Converter.toGGRS87(mytilhnh[0],mytilhnh[1]),0.01);
    } @Test
    public void toGGRS87Athens() {
        assertArrayEquals(athensEGSA,WGS84Converter.toGGRS87(athens[0],athens[1]),0.01);
    }
}