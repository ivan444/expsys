package imaing.expsys.client.view.widgets;

import imaing.expsys.client.domain.AndClause;
import imaing.expsys.client.domain.Characteristic;
import imaing.expsys.client.domain.Literal;
import imaing.expsys.client.domain.LogClause;
import imaing.expsys.client.domain.NotClause;
import imaing.expsys.client.domain.OrClause;
import imaing.expsys.client.domain.Rule;
import imaing.expsys.client.domain.Shop;
import imaing.expsys.shared.Relevance;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.uibinder.client.UiField;
import com.google.gwt.uibinder.client.UiHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.SimplePanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.Widget;

public class RuleWidget extends Composite {
	
	private static RuleWidgetUiBinder uiBinder = GWT.create(RuleWidgetUiBinder.class);

	interface RuleWidgetUiBinder extends UiBinder<Widget, RuleWidget> {
	}
	
	public interface WidgetRuleManager {
		void saveRule(Rule r);
		void deleteRule(Rule r);
	}

	@UiField
	HTMLPanel descPane;
	
	@UiField
	HTMLPanel rulePane;
	
	@UiField
	Button btnDelete;
	
	@UiField
	Button btnSave;
	
	private Rule rule;
	private WidgetRuleManager wRuleMan;
	final private List<Characteristic> characteristics;
	private ClauseWidget clauseBuilder;
	
	private final String REL_HIGH = "HIGH";
	private final String REL_MID = "MEDIUM";
	private final String REL_LOW = "LOW";
	
	
	public RuleWidget(Shop shop, List<Characteristic> characteristics) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.characteristics = characteristics;
		this.rule = new Rule();
		this.rule.setShop(shop);
		buildNewRuleForm();
	}
	
	private void buildNewRuleForm() {
		final TextBox desc = new TextBox();
		desc.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				rule.setDesc(((TextBox)event.getSource()).getText());
			}
		});
		this.descPane.add(desc);
		
		this.btnSave.setVisible(true);
		
		HorizontalPanel ruleParts = new HorizontalPanel();
		this.clauseBuilder = new ClauseWidget(this);
		
		final ListBox relLst = new ListBox();
		relLst.addItem("Priority");
		relLst.addItem(REL_HIGH);
		relLst.addItem(REL_MID);
		relLst.addItem(REL_LOW);
		relLst.addChangeHandler(new ChangeHandler() {
			@Override
			public void onChange(ChangeEvent event) {
				int selIdx = relLst.getSelectedIndex();
				if (selIdx == -1) return;
				
				String selItem = relLst.getItemText(selIdx);
				if (REL_HIGH.equals(selItem)) rule.setRel(Relevance.REL_HIGH);
				else if (REL_MID.equals(selItem)) rule.setRel(Relevance.REL_MID);
				else if (REL_LOW.equals(selItem)) rule.setRel(Relevance.REL_LOW);
			}
		});
		
		ruleParts.add(clauseBuilder);
		ruleParts.add(new Label(", Priority: "));
		ruleParts.add(relLst);
		
		this.rulePane.add(ruleParts);
	}
	
	public RuleWidget(Rule rule) {
		initWidget(uiBinder.createAndBindUi(this));
		
		this.characteristics = null;
		this.rule = rule;
		this.btnSave.setVisible(false);
		showRule(rule);
	}
	
	public void updateRuleId(Long id) {
		this.rule.setId(id);
	}
	
	private void showRule(Rule rule) {
		Label descLbl = new Label(rule.getDesc());
		Label ruleLbl = new Label(rule.toString());
		descPane.add(descLbl);
		rulePane.add(ruleLbl);
	}

	public void setwRuleMan(WidgetRuleManager wRuleMan) {
		this.wRuleMan = wRuleMan;
	}
	
	@UiHandler("btnDelete")
	void handleDelClick(ClickEvent e) {
		if (wRuleMan != null) {
			if (rule.getId() != null) {
				wRuleMan.deleteRule(rule);
			}
			this.removeFromParent();
		} else {
			GWT.log("WidgetRuleManager is not set!");
		}
	}
	
	@UiHandler("btnSave")
	void handleSaveClick(ClickEvent e) {
		if (wRuleMan != null) {
			wRuleMan.saveRule(rule);
			this.descPane.clear();
			this.rulePane.clear();
			this.btnSave.setVisible(false);
			showRule(rule);
		} else {
			GWT.log("WidgetRuleManager is not set!");
		}
	}
	
	private class ClauseWidget extends SimplePanel {
		protected final String AND = "AND";
		protected final String OR = "OR";
		protected final String NOT = "NOT";
		protected final String LITERAL = "LITERAL";
		
		protected final RuleWidget topWidget;
		protected ClauseWidget firstChild = null;
		protected final SimplePanel me;
		
		public ClauseWidget(final RuleWidget topWidget) {
			super();
			this.topWidget = topWidget;
			this.me = this;
			init();
		}
		
		protected void init() {
			final ListBox clauses = new ListBox();
			clauses.addItem("Operator");
			clauses.addItem(AND);
			clauses.addItem(OR);
			clauses.addItem(NOT);
			clauses.addItem(LITERAL);
			
			clauses.addChangeHandler(new ChangeHandler() {
				@Override
				public void onChange(ChangeEvent event) {
					int selIdx = clauses.getSelectedIndex();
					if (selIdx == -1) return;
					
					String selItem = clauses.getItemText(selIdx);
					firstChild = buildClauseInstanceWidget(selItem);
					if (firstChild != null) {
						me.clear();
						me.add(firstChild);
					}
				}
			});
			
			me.add(clauses);
		}
		
		protected ClauseWidget buildClauseInstanceWidget(String oper) {
			if (AND.equals(oper)) {
				return new ClauseWidget(topWidget) {
					private ClauseWidget secondChild;
					
					protected void init() {
						HorizontalPanel clausePane = new HorizontalPanel();
						
						firstChild = new ClauseWidget(topWidget);
						secondChild = new ClauseWidget(topWidget);
						
						clausePane.add(new Label("AND( "));
						clausePane.add(firstChild);
						clausePane.add(new Label(", "));
						clausePane.add(secondChild);
						clausePane.add(new Label(" )"));
						
						add(clausePane);
					}
					
					public boolean isComplete() {
						return firstChild != null && secondChild != null
								&& firstChild.isComplete() && secondChild.isComplete();
					}
					
					public LogClause buildLogClause() {
						return new AndClause(firstChild.buildLogClause(), secondChild.buildLogClause());
					}
				};
				
			} else if (OR.equals(oper)) {
				return new ClauseWidget(topWidget) {
					private ClauseWidget secondChild;
					
					protected void init() {
						HorizontalPanel clausePane = new HorizontalPanel();
						
						firstChild = new ClauseWidget(topWidget);
						secondChild = new ClauseWidget(topWidget);
						
						clausePane.add(new Label("OR( "));
						clausePane.add(firstChild);
						clausePane.add(new Label(", "));
						clausePane.add(secondChild);
						clausePane.add(new Label(" )"));
						
						add(clausePane);
					}
					
					public boolean isComplete() {
						boolean ret= firstChild != null && secondChild != null
								&& firstChild.isComplete() && secondChild.isComplete();
						Window.alert("OR is " + ret
								+ "\n" + (firstChild != null)
								+ "\n" + (secondChild != null)
								+ "\n" + (firstChild == null ? false : firstChild.isComplete())
								+ "\n" + (secondChild == null ? false : secondChild.isComplete()));
						return ret;
					}
					
					public LogClause buildLogClause() {
						return new OrClause(firstChild.buildLogClause(), secondChild.buildLogClause());
					}
				};
				
			} else if (NOT.equals(oper)) {
				return new ClauseWidget(topWidget) {
					
					protected void init() {
						HorizontalPanel clausePane = new HorizontalPanel();
						
						firstChild = new ClauseWidget(topWidget);
						
						clausePane.add(new Label("NOT( "));
						clausePane.add(firstChild);
						clausePane.add(new Label(" )"));
						
						add(clausePane);
					}
					
					public boolean isComplete() {
						return firstChild != null && firstChild.isComplete();
					}
					
					public LogClause buildLogClause() {
						return new NotClause(firstChild.buildLogClause());
					}
				};
				
			} else if (LITERAL.equals(oper)) {
				return new ClauseWidget(topWidget) {
					private Characteristic chr = null;
					private int choosenCls = -1;
					private HorizontalPanel clausePane;
					
					protected void init() {
						clausePane = new HorizontalPanel();
						final ListBox chrList = new ListBox();
						chrList.addItem("Characteristic");
						for (int i = 0; i < characteristics.size(); i++) {
							chrList.addItem(characteristics.get(i).getName(), String.valueOf(i));
						}
						
						chrList.addChangeHandler(new ChangeHandler() {
							@Override
							public void onChange(ChangeEvent event) {
								int selIdx = chrList.getSelectedIndex();
								if (selIdx == -1) return;
								
								int chrIdx = Integer.parseInt(chrList.getValue(selIdx));
								chr = characteristics.get(chrIdx);
								clausePane.clear();
								clausePane.add(new Label(chr.getName()));
								clausePane.add(new Label(" = "));
								
								final ListBox fclsList = new ListBox();
								fclsList.addItem("Class");
								for (int i = 0; i < chr.getfClsNum(); i++) {
									fclsList.addItem(String.valueOf(i));
								}
								fclsList.addChangeHandler(new ChangeHandler() {
									@Override
									public void onChange(ChangeEvent event) {
										int fcSelIdx = fclsList.getSelectedIndex();
										if (fcSelIdx == -1) return;
										
										choosenCls = Integer.parseInt(fclsList.getValue(fcSelIdx));
										fclsList.removeFromParent();
										clausePane.add(new Label(String.valueOf(choosenCls)));
										
										if (topWidget.getClauseBuilder().isComplete()) {
											topWidget.buildRuleLogClause();
										}
									}
								});
								clausePane.add(fclsList);
							}
						});
						
						clausePane.add(chrList);
						clausePane.add(new Label(" = "));
						ListBox disLb = new ListBox();
						disLb.setEnabled(false);
						clausePane.add(disLb);
						
						add(clausePane);
					}
					
					public boolean isComplete() {
						return chr != null && choosenCls != -1;
					}
					
					public LogClause buildLogClause() {
						return new Literal(chr, Integer.valueOf(choosenCls));
					}
				};
			}
			
			return null;
		}
		
		public boolean isComplete() {
			return firstChild != null && firstChild.isComplete();
		}
		
		public LogClause buildLogClause() {
			return firstChild.buildLogClause();
		}
		
	}
	
	private ClauseWidget getClauseBuilder() {
		return clauseBuilder;
	}
	
	private void buildRuleLogClause() {
		rule.setLogClause(clauseBuilder.buildLogClause());
		Window.alert("Log clause is built! " + rule.getLogClause());
		clauseBuilder = null;
	}
	
}
