package bitchanger.gui.controller;

import java.util.ArrayDeque;

import bitchanger.calculations.ConversionStep;
import bitchanger.calculations.ConvertingNumbers;
import bitchanger.gui.views.CalcPathView;
import bitchanger.preferences.Preferences;
import javafx.beans.property.ReadOnlyIntegerProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.control.TextField;

public class CalcPathViewController extends ControllerBase<CalcPathView> {

	
	private final ReadOnlyIntegerProperty fromBaseProperty;
	private final ReadOnlyIntegerProperty toBaseProperty;
	private TextField tfInput;
	private TextField tfOutput;
	
	
	public CalcPathViewController(CalcPathView controllable) {
		super(controllable);
		this.fromBaseProperty = controllable.fromBaseProperty();
		this.toBaseProperty = controllable.toBaseProperty();
	}

	@Override
	protected void initControls() {
		this.tfInput = textFieldMap.get(controllable.tfInputKey());
		this.tfOutput = textFieldMap.get(controllable.tfOutputKey());
	}
	
	
	
	@Override
	public void setActions() {
		setInputAction();
		setBaseListener();
	}

	private void setInputAction() {
		tfInput.textProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
				updateValue();
			}
		});
	}

	private void updateValue() {
		String output = "";
		ArrayDeque<ConversionStep> calcPath = new ArrayDeque<>();
		
		if(fromBaseProperty.get() == 10) {
			output = ConvertingNumbers.decToBase(toBaseProperty.get(), tfInput.getText(), Preferences.getPrefs().getComma(), 16, calcPath);
		} else if(toBaseProperty.get() == 10){
			output = ConvertingNumbers.baseToDecString(fromBaseProperty.get(), tfInput.getText(), Preferences.getPrefs().getComma(), calcPath);
		} else {
			// TODO: 2 beliebige Basen umrechnen über Basis 10
		}
		
		tfOutput.setText(output);
		
		for(ConversionStep cs : calcPath) {
			System.out.println(cs);
			System.out.println();
		}
		System.out.println();
		System.out.println();
		
		calcPath.add(new ConversionStep("Das Ergebnis ist : " + output, true));
		
		controllable.setCalcPath(calcPath);
	}
	
	private void setBaseListener() {
		fromBaseProperty.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				updateValue();
			}
		});
		
		toBaseProperty.addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				updateValue();
			}
		});
	}


}
