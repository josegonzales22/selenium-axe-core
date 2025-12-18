const fs = require('fs');
const path = require('path');
const { createHtmlReport } = require('axe-html-reporter');

const inputFile = process.argv[2];
const stepName = process.argv[3];

if (!inputFile || !stepName) {
  console.error('Uso: node generate-html.js <input.json> <stepName>');
  process.exit(1);
}

// Leer resultados del JSON
const results = JSON.parse(fs.readFileSync(inputFile, 'utf-8'));

// Carpeta fija para todos los HTML
const artifactsDir = path.join(__dirname, '../artifacts');
if (!fs.existsSync(artifactsDir)) fs.mkdirSync(artifactsDir, { recursive: true });

// ⚡ Siempre genera accessibilityReport.html
const artifactFile = path.join(artifactsDir, 'accessibilityReport.html');

try {
  createHtmlReport({
    violations: results.violations || [],
    passes: results.passes || [],
    incomplete: results.incomplete || [],
    inapplicable: results.inapplicable || [],
    url: results.url || '',
    options: {
      outputDir: artifactsDir,
      reportFileName: 'accessibilityReport.html', // siempre el mismo archivo
      reportTitle: `Reporte de Accesibilidad - ${stepName}`
    }
  });

  console.log('✅ HTML generado en', artifactFile);
  process.exit(0); // éxito
} catch (err) {
  console.error('❌ Error al generar HTML:', err);
  process.exit(1); // indica fallo a Java
}
