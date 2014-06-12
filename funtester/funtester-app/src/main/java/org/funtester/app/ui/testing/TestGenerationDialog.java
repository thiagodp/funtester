package org.funtester.app.ui.testing;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import org.funtester.app.i18n.Messages;
import org.funtester.app.project.AppState;
import org.funtester.app.project.FrameworkInfo;
import org.funtester.app.project.PluginInfo;
import org.funtester.app.ui.common.DefaultEditingDialog;
import org.funtester.app.ui.common.DefaultFileChooser;
import org.funtester.app.ui.common.DefaultFileNameExtensionFilters;
import org.funtester.app.ui.common.ImagePath;
import org.funtester.app.ui.util.ImageUtil;
import org.funtester.app.validation.TestGenerationConfigurationValidator;
import org.funtester.common.generation.TestGenerationConfiguration;
import org.funtester.common.util.Validator;

import com.jgoodies.forms.factories.FormFactory;
import com.jgoodies.forms.layout.ColumnSpec;
import com.jgoodies.forms.layout.FormLayout;
import com.jgoodies.forms.layout.RowSpec;

/**
 * Test Generation Dialog.
 * 
 * @author Thiago Delgado Pinto
 *
 */
@SuppressWarnings({"rawtypes", "unchecked"})
public class TestGenerationDialog extends
		DefaultEditingDialog< TestGenerationConfiguration > {
	
	private static final long serialVersionUID = 6132709329561662391L;
	
	private boolean configurationMode = true; 
	private final AppState appState;
	private final List< PluginInfo > plugins;
	private final Map< Integer, List< String > > frameworkIndexToValuesMap;
	private boolean currentPluginCanRunTestsInternally = false;
	
	//
	// Abstract test generation
	//
	private final JRadioButton createNewFileOption;
	private final JRadioButton useExistingFileOption;
	private final JTextField semanticTestFile;
	private final JButton chooseAbstractTestFileButton;
	
	//
	// Plug-in
	//
	private final JComboBox plugin;
	private final JComboBox testingFramework;
	
	//
	// Code generation
	//
	private final JCheckBox generateCodeOption;
	private final JTextField outputDirectory;
	private final JButton chooseOutputDirectoryButton;
	private final JTextField mainClass;
	private final JTextField packageName;
	private final JSpinner timeoutInMS;
	
	//
	// Run
	//
	private final JCheckBox runOption;
	private final JRadioButton useCommandsOption;
	private final JRadioButton runInternallyOption;
	private final JTextArea commandsToRun;
	private final JTextField originalResultFile;
	private final JButton chooseOriginalResultFileButton;
	private final JTextField convertedResultFile;
	private final JButton chooseConvertedResultFileButton;
	private JTextField testSuiteName;
	
	
	/**
	 * Create the dialog
	 */
	public TestGenerationDialog(
			AppState appState
			) {
		setSize(new Dimension(870, 690));
		setAlwaysOnTop( true );
		
		this.appState = appState;

		plugins = new ArrayList< PluginInfo >();
		frameworkIndexToValuesMap = new LinkedHashMap< Integer, List< String > >();
		
		final List< String > pluginValues = new ArrayList< String >();
		
		int index = 0;
		for ( PluginInfo p : appState.getPluginMap().keySet() ) {
			
			plugins.add( p );
			
			String pValue = p.getName() + " " + p.getVersion();
			
			pluginValues.add( pValue );
			
			// Frameworks
			List< String > values = new ArrayList< String >();
			for ( FrameworkInfo f : p.getFrameworks() ) {
				values.add( f.getName() );
			}
			
			frameworkIndexToValuesMap.put( index, values );
			++index;
		}
		
		
		
		okButton.setText(Messages.getString("TestGenerationDialog.thisOkButton.text")); //$NON-NLS-1$
		
		setTitle(Messages.getString("TestGenerationDialog.this.title")); //$NON-NLS-1$
		setIconImage( ImageUtil.loadImage( ImagePath.testRunIcon() ) );
		
		contentPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.UNRELATED_GAP_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.UNRELATED_GAP_COLSPEC,},
			new RowSpec[] {
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.UNRELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				RowSpec.decode("default:grow"),
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JPanel abstractTestGenerationPanel = new JPanel();
		contentPanel.add(abstractTestGenerationPanel, "3, 2, 11, 1, fill, fill");
		abstractTestGenerationPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.MIN_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.GROWING_BUTTON_COLSPEC,},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,}));
		
		//
		// Abstract test generation
		//
		
		JSeparator separator = new JSeparator();
		separator.setMinimumSize(new Dimension(20, 10));
		abstractTestGenerationPanel.add(separator, "1, 1");
		
		JLabel lblAbstractTestGeneration = new JLabel(Messages.getString("TestGenerationDialog.lblAbstractTestGeneration.text")); //$NON-NLS-1$
		lblAbstractTestGeneration.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblAbstractTestGeneration.setForeground(Color.DARK_GRAY);
		lblAbstractTestGeneration.setIcon( null );
		abstractTestGenerationPanel.add(lblAbstractTestGeneration, "3, 1");
		
		JSeparator separator1 = new JSeparator();
		abstractTestGenerationPanel.add(separator1, "5, 1");
		
		ButtonGroup fileOptionGroup = new ButtonGroup();
		
		createNewFileOption = new JRadioButton(Messages.getString("TestGenerationDialog.createNewFileOption.text")); //$NON-NLS-1$
		createNewFileOption.setName("createNewFileOption");
		contentPanel.add(createNewFileOption, "7, 4");
		fileOptionGroup.add( createNewFileOption );
		createNewFileOption.setSelected(true);
		
		useExistingFileOption = new JRadioButton(Messages.getString("TestGenerationDialog.useExistingFileOption.text")); //$NON-NLS-1$
		useExistingFileOption.setName("useExistingFileOption");
		contentPanel.add(useExistingFileOption, "9, 4");
		fileOptionGroup.add( useExistingFileOption );
		
		JLabel lblSemanticTestFile = new JLabel(Messages.getString("TestGenerationDialog.lblSemanticTestFile.text")); //$NON-NLS-1$
		contentPanel.add(lblSemanticTestFile, "5, 6, right, default");
		
		semanticTestFile = new JTextField();
		semanticTestFile.setName("semanticTestFile");
		contentPanel.add(semanticTestFile, "7, 6, 5, 1");
		semanticTestFile.setColumns(10);
		
		chooseAbstractTestFileButton = new JButton("...");
		chooseAbstractTestFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				DefaultFileChooser.chooseFile(
						semanticTestFile,
						DefaultFileNameExtensionFilters.ABSTRACT_TESTS,
						createNewFileOption.isSelected()
						);
			}
		});
		chooseAbstractTestFileButton.setName("chooseAbstractTestFileButton");
		contentPanel.add(chooseAbstractTestFileButton, "13, 6");
		
		//
		// Plug-in
		//
		
		plugin = new JComboBox( new DefaultComboBoxModel(
				pluginValues.toArray( new String[ 0 ] ) ) );
		
		plugin.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				if ( e.getStateChange() != ItemEvent.SELECTED ) { return; }
				int index = plugin.getSelectedIndex();
				if ( index < 0 ) { return; }
				populateWithFrameworksFromThePluginAt( index );
			}
		});
		
		JLabel lblTestSuiteName = new JLabel(Messages.getString("TestGenerationDialog.lblTestSuiteName.text")); //$NON-NLS-1$
		lblTestSuiteName.setForeground(Color.GRAY);
		contentPanel.add(lblTestSuiteName, "5, 8, right, default");
		
		// Not editable for now
		testSuiteName = new JTextField();
		testSuiteName.setEditable(false);
		testSuiteName.setEnabled(false);
		testSuiteName.setName("testSuiteName");
		contentPanel.add(testSuiteName, "7, 8, 5, 1, fill, default");
		testSuiteName.setColumns(10);
		testSuiteName.setText( appState.getConfiguration().getDefaultTestSuiteName() );
		
		JPanel pluginPanel = new JPanel();
		contentPanel.add(pluginPanel, "3, 11, 11, 1, fill, fill");
		pluginPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.MIN_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JSeparator separator6 = new JSeparator();
		separator6.setMinimumSize(new Dimension(20, 10));
		pluginPanel.add(separator6, "1, 1");
		
		JLabel lblPluginForGenerationOrExecution = new JLabel(Messages.getString("TestGenerationDialog.lblPluginForGenerationOrExecution.text")); //$NON-NLS-1$
		lblPluginForGenerationOrExecution.setFont(new Font("Tahoma", Font.BOLD, 13));
		lblPluginForGenerationOrExecution.setForeground(Color.DARK_GRAY);
		pluginPanel.add(lblPluginForGenerationOrExecution, "3, 1");
		
		JSeparator separator7 = new JSeparator();
		pluginPanel.add(separator7, "5, 1");
		
		JLabel lblPlugin = new JLabel("Plug-in:");
		contentPanel.add(lblPlugin, "5, 13, right, default");
		plugin.setName("plugin");
		contentPanel.add(plugin, "7, 13, 5, 1");
		
		JLabel lblTestingFramework = new JLabel(Messages.getString("TestGenerationDialog.lblTestingFramework.text")); //$NON-NLS-1$
		contentPanel.add(lblTestingFramework, "5, 15, right, default");
		
		testingFramework = new JComboBox();
		testingFramework.setName("testingFramework");
		contentPanel.add(testingFramework, "7, 15, 5, 1");
		
		//
		// Code generation
		//
		
		JPanel codeGenerationPanel = new JPanel();
		contentPanel.add(codeGenerationPanel, "3, 18, 11, 1, fill, fill");
		codeGenerationPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.MIN_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JSeparator separator4 = new JSeparator();
		separator4.setMinimumSize(new Dimension(20, 10));
		codeGenerationPanel.add(separator4, "1, 1");
		
		generateCodeOption = new JCheckBox(Messages.getString("TestGenerationDialog.generateCodeOption.text")); //$NON-NLS-1$
		generateCodeOption.setFont(new Font("Tahoma", Font.BOLD, 13));
		generateCodeOption.setForeground(Color.DARK_GRAY);
		generateCodeOption.setMargin(new Insets(2, 0, 2, 2));
		generateCodeOption.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				setGenerateOptionsEnabledIf( e.getStateChange() == ItemEvent.SELECTED );
			}
		});
		generateCodeOption.setName("generationCodeOption");
		generateCodeOption.setSelected(true);
		codeGenerationPanel.add(generateCodeOption, "3, 1");
		
		JSeparator separator5 = new JSeparator();
		codeGenerationPanel.add(separator5, "5, 1");
		
		JLabel lblOutputDirectory = new JLabel(Messages.getString("TestGenerationDialog.lblOutputDirectory.text")); //$NON-NLS-1$
		contentPanel.add(lblOutputDirectory, "5, 20, right, default");
		
		outputDirectory = new JTextField();
		outputDirectory.setName("outputDirectory");
		contentPanel.add(outputDirectory, "7, 20, 5, 1, fill, default");
		outputDirectory.setColumns(10);
		
		chooseOutputDirectoryButton = new JButton("...");
		chooseOutputDirectoryButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultFileChooser.chooseDirectory( outputDirectory );
			}
		});
		chooseOutputDirectoryButton.setName("chooseOutputDirectoryButton");
		contentPanel.add(chooseOutputDirectoryButton, "13, 20");
		
		JLabel lblMainClass = new JLabel(Messages.getString("TestGenerationDialog.lblMainClass.text")); //$NON-NLS-1$
		contentPanel.add(lblMainClass, "5, 22, right, default");
		
		mainClass = new JTextField();
		mainClass.setName("mainClass");
		contentPanel.add(mainClass, "7, 22, 5, 1, fill, default");
		mainClass.setColumns(10);
		
		JLabel lblPackageName = new JLabel(Messages.getString("TestGenerationDialog.lblPackageName.text")); //$NON-NLS-1$
		contentPanel.add(lblPackageName, "5, 24, right, default");
		
		packageName = new JTextField();
		packageName.setName("packageName");
		contentPanel.add(packageName, "7, 24, 5, 1, fill, default");
		packageName.setColumns(10);
		
		JLabel lbTimeout = new JLabel(Messages.getString("TestGenerationDialog.lbTimeout.text")); //$NON-NLS-1$
		contentPanel.add(lbTimeout, "5, 26, right, default");
		
		timeoutInMS = new JSpinner();
		timeoutInMS.setName("timeoutInMS");
		timeoutInMS.setModel(new SpinnerNumberModel(5000, 0, 1000000, 100));
		contentPanel.add(timeoutInMS, "7, 26");
		
		JLabel lblMilisseconds = new JLabel(Messages.getString("TestGenerationDialog.lblMilisseconds.text")); //$NON-NLS-1$
		contentPanel.add(lblMilisseconds, "9, 26, left, default");
		
		//
		// Run
		//
		
		JPanel runPanel = new JPanel();
		contentPanel.add(runPanel, "3, 29, 11, 1, fill, fill");
		runPanel.setLayout(new FormLayout(new ColumnSpec[] {
				FormFactory.MIN_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				FormFactory.DEFAULT_COLSPEC,
				FormFactory.RELATED_GAP_COLSPEC,
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JSeparator separator2 = new JSeparator();
		separator2.setMinimumSize(new Dimension(20, 10));
		runPanel.add(separator2, "1, 1");
		
		runOption = new JCheckBox(Messages.getString("TestGenerationDialog.runOption.text")); //$NON-NLS-1$
		runOption.setForeground(Color.DARK_GRAY);
		runOption.setFont(new Font("Tahoma", Font.BOLD, 13));
		runOption.setMargin(new Insets(2, 0, 2, 2));
		runOption.setName("runOption");
		runOption.setSelected(true);
		runOption.addItemListener(new ItemListener() {
			public void itemStateChanged(ItemEvent e) {
				setRunOptionsEnabledIf( e.getStateChange() == ItemEvent.SELECTED );
			}
		});
		
		runPanel.add(runOption, "3, 1");
		
		JSeparator separator3 = new JSeparator();
		runPanel.add(separator3, "5, 1");
		
		ButtonGroup commandOptionGroup = new ButtonGroup();
		
		useCommandsOption = new JRadioButton(Messages.getString("TestGenerationDialog.useCommandsOption.text")); //$NON-NLS-1$
		useCommandsOption.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				commandsToRun.setEnabled( true );
			}
		});
		useCommandsOption.setSelected(true);
		contentPanel.add(useCommandsOption, "7, 31");
		commandOptionGroup.add( useCommandsOption );
		
		runInternallyOption = new JRadioButton(Messages.getString("TestGenerationDialog.runInternallyOption.text")); //$NON-NLS-1$
		runInternallyOption.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				commandsToRun.setEnabled( false );
			}
		});
		contentPanel.add(runInternallyOption, "9, 31");
		commandOptionGroup.add( runInternallyOption );
		
		JPanel commandLabelPanel = new JPanel();
		contentPanel.add(commandLabelPanel, "5, 33, fill, fill");
		commandLabelPanel.setLayout(new FormLayout(new ColumnSpec[] {
				ColumnSpec.decode("default:grow"),},
			new RowSpec[] {
				FormFactory.DEFAULT_ROWSPEC,
				FormFactory.RELATED_GAP_ROWSPEC,
				FormFactory.DEFAULT_ROWSPEC,}));
		
		JLabel lblCommand = new JLabel(Messages.getString("TestGenerationDialog.lblCommand.text")); //$NON-NLS-1$
		commandLabelPanel.add(lblCommand, "1, 1, right, default");
		
		JLabel lblOnePerLine = new JLabel(Messages.getString("TestGenerationDialog.lblOnePerLine.text")); //$NON-NLS-1$
		lblOnePerLine.setForeground(Color.GRAY);
		commandLabelPanel.add(lblOnePerLine, "1, 3, right, default");
		
		JScrollPane commandScrollPane = new JScrollPane();
		contentPanel.add(commandScrollPane, "7, 33, 5, 1, fill, fill");
		
		commandsToRun = new JTextArea();
		commandsToRun.setName("commandsToRun");
		commandScrollPane.setViewportView(commandsToRun);
		
		JLabel lblOriginalResultFile = new JLabel(Messages.getString("TestGenerationDialog.lblOriginalResultFile.text")); //$NON-NLS-1$
		contentPanel.add(lblOriginalResultFile, "5, 35, right, default");
		
		originalResultFile = new JTextField();
		originalResultFile.setName("originalResultFile");
		originalResultFile.setColumns(10);
		contentPanel.add(originalResultFile, "7, 35, 5, 1, fill, default");
		
		chooseOriginalResultFileButton = new JButton("...");
		chooseOriginalResultFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				DefaultFileChooser.chooseFile(
						originalResultFile,
						DefaultFileNameExtensionFilters.ANY,
						true // The file could still not exist
						);
			}
		});
		chooseOriginalResultFileButton.setName("chooseOriginalResultFileButton");
		contentPanel.add(chooseOriginalResultFileButton, "13, 35");
		
		JLabel lblConvertedResultFile = new JLabel(Messages.getString("TestGenerationDialog.lblConvertedResultFile.text")); //$NON-NLS-1$
		contentPanel.add(lblConvertedResultFile, "5, 37, right, default");
		
		convertedResultFile = new JTextField();
		convertedResultFile.setName("convertedResultFile");
		convertedResultFile.setColumns(10);
		contentPanel.add(convertedResultFile, "7, 37, 5, 1, fill, default");
		
		chooseConvertedResultFileButton = new JButton("...");
		chooseConvertedResultFileButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				DefaultFileChooser.chooseFile(
						convertedResultFile,
						DefaultFileNameExtensionFilters.TEST_EXECUTION_REPORT,
						true
						);
			}
		});
		chooseConvertedResultFileButton.setName("chooseConvertedResultFileButton");
		contentPanel.add(chooseConvertedResultFileButton, "13, 37");
		
		checkPlugins();
		
		//
		// Back to the normal mode
		//
		configurationMode = false;
	}
	
	private void checkPlugins() {
		if ( ! plugins.isEmpty() ) { return; }
		
		// No plug-ins, so it cannot generate code or run tests
		
		generateCodeOption.setSelected( false );
		
		runOption.setSelected( false );
	}

	@Override
	protected TestGenerationConfiguration createObject() {
		return new TestGenerationConfiguration();
	}

	@Override
	protected boolean populateObject() {

		TestGenerationConfiguration obj = getObject();
		
		obj.setSoftwareFile( appState.getProjectFileName() );
		
		// Semantic test generation options
		
		obj.setGenerateSemanticTests( createNewFileOption.isSelected() );
		obj.setSemanticTestFile( semanticTestFile.getText() );
		
		// Plug-in
		
		int pluginIndex = plugin.getSelectedIndex();
		PluginInfo plugin = ( pluginIndex >= 0 ) ? plugins.get( pluginIndex ) : null;
		String pluginId = ( plugin != null ) ? plugin.getId() : "";
		obj.setPluginId( pluginId );
		
		int frameworkIndex = testingFramework.getSelectedIndex();
		FrameworkInfo frameworkInfo = ( plugin != null && frameworkIndex >= 0 )
				? plugin.getFrameworks().get( frameworkIndex )
				: null; 
		String frameworkId = ( frameworkInfo != null ) ? frameworkInfo.getId() : "";
		obj.setTestingFramework( frameworkId );
		
		// Source code generation options
		
		obj.setGenerateCode( generateCodeOption.isSelected() );
		obj.setOutputDirectory( outputDirectory.getText() );
		obj.setMainClass( mainClass.getText() );
		obj.setPackageName( packageName.getText() );
		obj.setTimeoutInMS( (Integer) timeoutInMS.getValue() );
		
		// Test code execution options
		
		obj.setRun( runOption.isSelected() );
		
		// JTextArea doesn't use System.lineSeparator() so it have to split
		// using the "\n" character. Even though, this will replace all the
		// "\r" characters with an empty character.
		String[] lines = commandsToRun.getText().replaceAll( "\r", "" ).split( "\n" );
	
		obj.setCommandsLinesToRun( lines );
		obj.setTryToRunInternally( runInternallyOption.isSelected() );
		obj.setOriginalResultsFile( originalResultFile.getText() );
		obj.setConvertedResultsFile( convertedResultFile.getText() );
		
		return true;
	}
	
	
	@Override
	protected void drawObject(TestGenerationConfiguration obj) {
		
		// Abstract test generation
		
		if ( null == obj.getSemanticTestFile()
			|| obj.getSemanticTestFile().isEmpty() ) {
			createNewFileOption.setSelected( true );
		}
		else {
			/*
			final File atf = new File( obj.getSemanticTestFile() );
			if ( atf.exists() ) {
				useExistingFileOption.setSelected( true );
			} else {
				createNewFileOption.setSelected( true );
			}
			*/
			
			if ( obj.getGenerateSemanticTests() ) {
				createNewFileOption.setSelected( true );
			} else {
				useExistingFileOption.setSelected( true );
			}
		}
		
		semanticTestFile.setText( obj.getSemanticTestFile() );
		//chooseAbstractTestFileButton;
		
		// Plug-in
		
		int pluginIndex = -1;
		int frameworkIndex = -1;
		
		PluginInfo pluginInfo = appState.pluginWithId( obj.getPluginId() );
		if ( pluginInfo != null ) {
			
			pluginIndex = plugins.indexOf( pluginInfo );
		
			final String frameworkId = obj.getTestingFramework();
			int index = 0;
			for ( FrameworkInfo f : pluginInfo.getFrameworks() ) {
				if ( f.getId().equals( frameworkId ) ) {
					frameworkIndex = index;
					break;
				}
				++index;
			}
		}
		
		plugin.setSelectedIndex( pluginIndex );
		if ( pluginIndex >= 0 ) {
			populateWithFrameworksFromThePluginAt( pluginIndex );
		}
		testingFramework.setSelectedIndex( frameworkIndex );
		
		// Code generation

		generateCodeOption.setSelected( obj.getGenerateCode() );
		outputDirectory.setText( obj.getOutputDirectory() );
		//chooseOutputDirectoryButton;
		mainClass.setText( obj.getMainClass() );
		packageName.setText( obj.getPackageName() );
		timeoutInMS.setValue( obj.getTimeoutInMS() );
		
		// Run

		runOption.setSelected( obj.getRun() );
		
		commandsToRun.setText( "" );
		if ( obj.getCommandsToRun() != null && ! obj.getCommandsToRun().isEmpty() ) {
			
			// JTextArea doesn't use System.lineSeparator() but "\n"
			
			StringBuffer sb = new StringBuffer();
			for ( String cmd : obj.getCommandsToRun() ) {
				sb.append( cmd ).append( "\n" ); // Not System.lineSeparator() 
			}
			commandsToRun.setText( sb.toString() );
		}
		
		runInternallyOption.setSelected( obj.getTryToRunInternally() );
		originalResultFile.setText( obj.getOriginalResultsFile() );
		//chooseOriginalResultFileButton
		convertedResultFile.setText( obj.getConvertedResultsFile() );
		//chooseConvertedResultFileButton;
	}

	
	@Override
	protected Validator< TestGenerationConfiguration > createValidator() {
		return new TestGenerationConfigurationValidator();
	}
	
	
	private void populateWithFrameworksFromThePluginAt(final int index) {
		if ( configurationMode || index < 0 ) { return; }
		
		List< String > values = frameworkIndexToValuesMap.get( index );
		
		if ( values != null ) {
			testingFramework.setModel( new DefaultComboBoxModel(
				values.toArray(new String[ 0 ] ) ) );
		} else {
			testingFramework.setModel( new DefaultComboBoxModel() );
		}
		
		testingFramework.setSelectedIndex( -1 );
		
		PluginInfo pluginInfo = plugins.get( index );
		if ( null == pluginInfo ) { return; }
		
		this.currentPluginCanRunTestsInternally = pluginInfo.getCanRunTestsInternally();
		runInternallyOption.setEnabled( currentPluginCanRunTestsInternally );
	}
	
	
	private void setGenerateOptionsEnabledIf(final boolean enabled) {
		if ( configurationMode ) { return; }
		
		//generateCodeOption.setEnabled( enabled );
		outputDirectory.setEnabled( enabled );
		chooseOutputDirectoryButton.setEnabled( enabled );
		mainClass.setEnabled( enabled );
		packageName.setEnabled( enabled );
		timeoutInMS.setEnabled( enabled );
	}

	
	private void setRunOptionsEnabledIf(final boolean enabled) {
		if ( configurationMode ) { return; }
		
		//runOption.setEnabled( enabled );
		useCommandsOption.setEnabled( enabled );
		runInternallyOption.setEnabled( enabled && currentPluginCanRunTestsInternally );
		commandsToRun.setEnabled( enabled );
		originalResultFile.setEnabled( enabled );
		chooseOriginalResultFileButton.setEnabled( enabled );
		convertedResultFile.setEnabled( enabled );
		chooseConvertedResultFileButton.setEnabled( enabled );
	}
}