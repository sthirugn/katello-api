package com.redhat.qe.katello.tests.cli;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.redhat.qe.Assert;
import com.redhat.qe.katello.base.KatelloCliTestBase;
import com.redhat.qe.katello.base.obj.KatelloContentDefinition;
import com.redhat.qe.katello.base.obj.KatelloContentView;
import com.redhat.qe.katello.base.obj.KatelloPermission;
import com.redhat.qe.katello.base.obj.KatelloSystem;
import com.redhat.qe.katello.base.obj.KatelloUser;
import com.redhat.qe.katello.base.obj.KatelloUserRole;
import com.redhat.qe.katello.base.tngext.TngPriority;
import com.redhat.qe.katello.common.KatelloUtils;
import com.redhat.qe.katello.common.TngRunGroups;

@TngPriority(10)
@Test(groups={"cfse-cli",TngRunGroups.TNG_KATELLO_Content})
public class ContentDefAccessTests extends KatelloCliTestBase{
	private String user_create;
	private String role_create;
	private String perm_create;
	private String user_modify;
	private String role_modify;
	private String perm_modify;
	private String content_name1;
	private String content_name2;
	private String content_name3;
	private String content_name4;
	private String user_delete;
	private String role_delete;
	private String perm_delete;
	private String user_read;
	private String role_read;
	private String perm_read;
	private String user_publish;
	private String role_publish;
	private String perm_publish;
	private String content_publish1;
	private String content_publish2;
	private String user_rpub;
	private String role_rpub;
	private String perm_rpub_v;
	private String perm_rpub_d;
	private String perm_rpub_o;
	private String content_rpub1;
	private String content_rpub2;
	private String view_name1;
	private String view_name2;
	private String content_pro1;
	private String user_prom;
	private String role_prom;
	private String perm_prom_v;
	private String perm_prom_d;
	private String perm_prom_o;
	private String view_pub1;
	private String view_pub2;
	private String sys_name;
	private String subscr_view1;
	private String subscr_view2;
	private String perm_subscr_o;
	private String perm_subscr_v1;
	private String perm_subscr_v2;
	private String subscr_user;
	private String subscr_role;

	@BeforeClass(description="init: create initial stuff", alwaysRun=true)
	public void setUp() {
		String uid = KatelloUtils.getUniqueID();
		user_create = "user1"+uid;
		role_create = "role1"+uid;
		perm_create = "perm1"+uid;
		user_delete = "user2"+uid;
		role_delete = "role2"+uid;
		perm_delete = "perm2"+uid;
		user_read = "user3"+uid;
		role_read = "role3"+uid;
		perm_read = "perm3"+uid;
		content_name1 = "content1"+uid;
		content_name2 = "content2"+uid;
		content_name3 = "content3"+uid;
		content_name4 = "content4"+uid;
		content_publish1 = "contentp1"+uid;
		content_publish2 = "contentp2"+uid;
		user_publish = "user5"+uid;
		role_publish = "role5"+uid;
		perm_publish = "perm5"+uid;

		user_modify = "user4"+uid;
		role_modify = "role4"+uid;
		perm_modify = "perm4"+uid;

		user_rpub = "userrp"+uid;
		role_rpub = "rolerp"+uid;
		perm_rpub_v = "permrp1"+uid;
		perm_rpub_d = "permrp2"+uid;
		perm_rpub_o = "permrp3"+uid;
		content_rpub1 = "contentrp1"+uid;
		content_rpub2 = "contentrp2"+uid;
		view_name1 = "view1"+uid;
		view_name2 = "view2"+uid;

		content_pro1 = "contentpro1"+uid;
		user_prom = "userpr"+uid;
		role_prom = "rolepr"+uid;
		perm_prom_v = "permprv"+uid;
		perm_prom_d = "permprd"+uid;
		perm_prom_o = "permpro"+uid;
		view_pub1=  "view1";
		view_pub2=  "view2";
		
		sys_name = "sys1";
		subscr_view1 = "viewsubscr1";
		subscr_view2 = "viewsubscr2";
		perm_subscr_o = "permo";
		perm_subscr_v1 = "permc1";
		perm_subscr_v2 = "permc2";
		subscr_user = "usersubsc"+uid;
		subscr_role = "rolesubscr"+uid;
	}
	
	@Test(description="initialization goes here")
	public void init(){
		KatelloUser user = new KatelloUser(cli_worker, this.user_create, "root@localhost", KatelloUser.DEFAULT_USER_PASS, false);
		exec_result = user.cli_create();
		Assert.assertTrue(exec_result.getExitCode().intValue()==0, "Check - return code (user create)");
		
		KatelloUserRole role = new KatelloUserRole(cli_worker, this.role_create, "create content definition");
		exec_result = role.create();
		Assert.assertTrue(exec_result.getExitCode().intValue()==0, "Check - return code (user_role create)");
		
		KatelloPermission perm = new KatelloPermission(cli_worker, perm_create, base_org_name, "content_view_definitions", null,
				"read,create", this.role_create);
		exec_result = perm.create();
		Assert.assertTrue(exec_result.getExitCode().intValue()==0, "Check - return code (perm create)");
		
		perm = new KatelloPermission(cli_worker, "org_perm"+KatelloUtils.getUniqueID(), base_org_name, "organizations", null,
				"delete_distributors,delete_systems,update_distributors,update,update_systems,read_distributors,read,read_systems,register_distributors,register_systems,sync", this.role_create);
		exec_result = perm.create();
		Assert.assertTrue(exec_result.getExitCode().intValue()==0, "Check - return code (perm create)");
		
		exec_result = user.assign_role(this.role_create);
		Assert.assertTrue(exec_result.getExitCode().intValue()==0, "Check - return code (user assign_role)");
		
		// user delete
		KatelloUser user2 = new KatelloUser(cli_worker, user_delete, "root@localhost", KatelloUser.DEFAULT_USER_PASS, false);
		exec_result = user2.cli_create();
		Assert.assertTrue(exec_result.getExitCode().intValue()==0, "Check - return code (user create)");

		KatelloUserRole role2 = new KatelloUserRole(cli_worker, role_delete, "delete content definition");
		exec_result = role2.create();
		Assert.assertTrue(exec_result.getExitCode().intValue()==0, "Check - return code (user_role create)");

		KatelloPermission perm2 = new KatelloPermission(cli_worker, perm_delete, base_org_name, "content_view_definitions", null,
				"read,delete", this.role_delete);
		exec_result = perm2.create(true);
		Assert.assertTrue(exec_result.getExitCode().intValue()==0, "Check - return code (perm create)");
		exec_result = user2.assign_role(role_delete);
		Assert.assertTrue(exec_result.getExitCode().intValue()==0, "Check - return code (user assign_role)");

		// user read
		KatelloUser user3 = new KatelloUser(cli_worker, user_read, "root@localhost", KatelloUser.DEFAULT_USER_PASS, false);
		exec_result = user3.cli_create();
		Assert.assertTrue(exec_result.getExitCode().intValue()==0, "Check - return code (user create)");
		
		KatelloUserRole role3 = new KatelloUserRole(cli_worker, role_read, "delete content definition");
		exec_result = role3.create();
		Assert.assertTrue(exec_result.getExitCode().intValue()==0, "Check - return code (user_role create)");

		KatelloPermission perm3 = new KatelloPermission(cli_worker, perm_read, base_org_name, "content_view_definitions", null,
				"read", this.role_read);
		exec_result = perm3.create(true);
		Assert.assertTrue(exec_result.getExitCode().intValue()==0, "Check - return code (perm create)");
		exec_result = user3.assign_role(role_read);
		Assert.assertTrue(exec_result.getExitCode().intValue()==0, "Check - return code (user assign_role)");

		// content to delete
		KatelloContentDefinition content = new KatelloContentDefinition(cli_worker, content_name3, "description", base_org_name, content_name3);
		exec_result = content.create();
		Assert.assertTrue(exec_result.getExitCode() == 0, "Check - return code");

		// publish user and stuff
		user = new KatelloUser(cli_worker, user_publish, "root@localhost", KatelloUser.DEFAULT_USER_PASS, false);
		exec_result = user.cli_create();

		role = new KatelloUserRole(cli_worker, role_publish, "create content definition");
		exec_result = role.create();
		Assert.assertTrue(exec_result.getExitCode().intValue()==0, "Check - return code (user_role create)");

		perm = new KatelloPermission(cli_worker, perm_publish, base_org_name, "content_view_definitions", null,
				"read,update,publish", role_publish);
		exec_result = perm.create(true);
		Assert.assertTrue(exec_result.getExitCode().intValue()==0, "Check - return code (perm create)");

		exec_result = user.assign_role(role_publish);
		Assert.assertTrue(exec_result.getExitCode().intValue()==0, "Check - return code (user assign_role)");

		content = new KatelloContentDefinition(cli_worker, content_publish1, "description", base_org_name, content_publish1);
		exec_result = content.create();
		Assert.assertTrue(exec_result.getExitCode() == 0, "Check - return code");
		content = new KatelloContentDefinition(cli_worker, content_publish2, "description", base_org_name, content_publish2);
		exec_result = content.create();
		Assert.assertTrue(exec_result.getExitCode() == 0, "Check - return code");

		// promote access stuff
		content = new KatelloContentDefinition(cli_worker, content_pro1, "description", base_org_name, content_pro1);
		exec_result = content.create();
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (create definition)");
		exec_result = content.publish(view_pub1, view_pub1, "description");
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (publish view)");
		exec_result = content.publish(view_pub2, view_pub2, "description");
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (publish view)");

		user = new KatelloUser(cli_worker, user_prom, KatelloUser.DEFAULT_USER_EMAIL, KatelloUser.DEFAULT_USER_PASS, false);
		exec_result = user.cli_create();
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (create user)");

		role = new KatelloUserRole(cli_worker, role_prom, "promote views");
		exec_result = role.create();
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (create role)");

		perm = new KatelloPermission(cli_worker, perm_prom_v, base_org_name, "content_views", view_pub1, "read,promote", role_prom);
		exec_result = perm.create();
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (create perm)");

		perm = new KatelloPermission(cli_worker, perm_prom_d, base_org_name, "content_view_definitions", null, "create,delete,update,publish,read", role_prom);
		exec_result = perm.create(true);
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (create perm)");

		perm = new KatelloPermission(cli_worker, perm_prom_o, base_org_name, "organizations", null, "read", role_prom);
		exec_result = perm.create(true);
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (create perm)");

		perm = new KatelloPermission(cli_worker, perm_prom_v+"r", base_org_name, "content_views", view_pub2, "read", role_prom);
		exec_result = perm.create();
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (create perm)");

		perm = new KatelloPermission(cli_worker, perm_prom_v+"-envs", base_org_name, "environments", null, "promote_changesets", role_prom);
		exec_result = perm.create(true); // with --all_tags
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (create perm)");

		exec_result = user.assign_role(role_prom);
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (assign role)");

		// stuff for modify tests
		content = new KatelloContentDefinition(cli_worker, content_name4, "description", base_org_name, content_name4);
		exec_result = content.create();
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (create definition)");

		role = new KatelloUserRole(cli_worker, role_modify, "description");
		exec_result = role.create();
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (create role)");

		perm = new KatelloPermission(cli_worker, perm_modify, base_org_name, "content_view_definitions", null, "read,update", role_modify);
		exec_result = perm.create(true);
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (create role)");

		user = new KatelloUser(cli_worker, user_modify, "root@localhost", KatelloUser.DEFAULT_USER_PASS, false);
		exec_result = user.cli_create();
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (create user)");

		exec_result = user.assign_role(role_modify);
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (assign role)");

		// read published access stuff
		content = new KatelloContentDefinition(cli_worker, content_rpub1, "description", base_org_name, content_rpub1);
		exec_result = content.create();
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (content create)");

		exec_result = content.publish(view_name1, view_name1, "description");
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (content publish)");

		content = new KatelloContentDefinition(cli_worker, content_rpub2, "description", base_org_name, content_rpub2);
		exec_result = content.create();
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (content create)");

		exec_result = content.publish(view_name2, view_name2, "description");
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (content publish)");

		user = new KatelloUser(cli_worker, user_rpub, KatelloUser.DEFAULT_USER_EMAIL, KatelloUser.DEFAULT_USER_PASS, false);
		exec_result = user.cli_create();
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (user create)");

		role = new KatelloUserRole(cli_worker, role_rpub, "read published content view");
		exec_result = role.create();
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (role create)");

		perm = new KatelloPermission(cli_worker, perm_rpub_v, base_org_name, "content_views", view_name1, "read", role_rpub);
		exec_result = perm.create();
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (perm create)");

		perm = new KatelloPermission(cli_worker, perm_rpub_d, base_org_name, "content_view_definitions", null, "create,delete,update,publish,read", role_rpub);
		exec_result = perm.create(true);
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (perm create)");

		perm = new KatelloPermission(cli_worker, perm_rpub_o, base_org_name, "organizations", null, "read", role_rpub);
		exec_result = perm.create(true);
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (perm create)");

		exec_result = user.assign_role(role_rpub);
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (assign role)");

		// subscribe access stuff
		KatelloContentDefinition def = new KatelloContentDefinition(cli_worker, subscr_view1, "description", base_org_name, subscr_view1);
		exec_result = def.create();
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (definition create)");
		exec_result = def.publish(subscr_view1, subscr_view1, "description");
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (definition publish)");
		
		KatelloContentView view = new KatelloContentView(cli_worker, subscr_view1, base_org_name);
		exec_result = view.promote_view(base_test_env_name);
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (view promote)");
		
		exec_result = def.publish(subscr_view2, subscr_view2, "description");
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (definition publish");
		
		view = new KatelloContentView(cli_worker, subscr_view2, base_org_name);
		exec_result = view.promote_view(base_test_env_name);
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (view promote)");

		role = new KatelloUserRole(cli_worker, subscr_role, "subscribe content view");
		exec_result = role.create();
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (create role)");
		perm = new KatelloPermission(cli_worker, perm_subscr_o, base_org_name, "organizations", null, "update_systems,read,read_systems", subscr_role);
		exec_result = perm.create();
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (permition create)");
		perm = new KatelloPermission(cli_worker, perm_subscr_v1, base_org_name, "content_views", subscr_view1, "read,subscribe", subscr_role);
		exec_result = perm.create();
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (permition create)");
		perm = new KatelloPermission(cli_worker, perm_subscr_v2, base_org_name, "content_views", subscr_view2, "read", subscr_role);
		exec_result = perm.create();
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (permition create)");

		user = new KatelloUser(cli_worker, subscr_user, KatelloUser.DEFAULT_USER_EMAIL, KatelloUser.DEFAULT_USER_PASS, false);
		exec_result = user.cli_create();
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (user create)");
		exec_result = user.assign_role(subscr_role);
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (assign role)");

		KatelloSystem sys = new KatelloSystem(this.cli_worker, sys_name, base_org_name, base_test_env_name);
		exec_result = sys.rhsm_registerForce();
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (register system)");
	}
	
	@Test(description = "check access in creating new content definition", dependsOnMethods={"init"})
	public void test_CreateAccess() {
		KatelloUser user = new KatelloUser(cli_worker, this.user_create, "root@localhost", KatelloUser.DEFAULT_USER_PASS, false);
		KatelloContentDefinition content = new KatelloContentDefinition(cli_worker, content_name1, "descritpion", base_org_name, content_name1);
		content.runAs(user);
		exec_result = content.create();
		Assert.assertTrue(exec_result.getExitCode() == 0, "Check - return code");
		Assert.assertTrue(getOutput(exec_result).equals(String.format(KatelloContentDefinition.OUT_CREATE_DEFINITION, content_name1)), "Check - out string (content create)");
	}

	@Test(description = "no access in creating new content definition", dependsOnMethods={"test_CreateAccess"})
	public void test_CreateNoAccess() {
		KatelloPermission perm = new KatelloPermission(cli_worker, perm_create, base_org_name, "content_view_definitions", null,
				"read,create", this.role_create);
		exec_result = perm.delete();
		Assert.assertTrue(exec_result.getExitCode().intValue()==0, "Check - return code (perm create)");
		
		perm = new KatelloPermission(cli_worker, perm_create, base_org_name, "content_view_definitions", null,
				"read", this.role_create);
		exec_result = perm.create();
		Assert.assertTrue(exec_result.getExitCode().intValue()==0, "Check - return code (perm create)");
		
		KatelloUser user = new KatelloUser(cli_worker, this.user_create, "root@localhost", KatelloUser.DEFAULT_USER_PASS, false);
		KatelloContentDefinition content = new KatelloContentDefinition(cli_worker, content_name2, "descritpion", base_org_name, content_name2);
		content.runAs(user);
		exec_result = content.create();
		Assert.assertTrue(exec_result.getExitCode() == 147, "Check - error code");
		Assert.assertTrue(getOutput(exec_result).equals(String.format(KatelloContentDefinition.ERR_CREATE_DENIED, this.user_create)), "Check - error string (content create)");
	}

	@Test(description="try to delete content definition without permissions", dependsOnMethods={"test_CreateAccess"})
	public void test_DeleteNoAccess() {
		KatelloUser user = new KatelloUser(cli_worker, user_read, "root@localhost", KatelloUser.DEFAULT_USER_PASS, false);
		KatelloContentDefinition content = new KatelloContentDefinition(cli_worker, content_name1, "description", base_org_name, content_name1);
		content.runAs(user);
		exec_result = content.delete();
		Assert.assertTrue(exec_result.getExitCode()==147, "Check = error code (delete content definition)");
		Assert.assertTrue(getOutput(exec_result).equals(String.format(KatelloContentDefinition.ERR_DELETE_DENIED, this.user_read)), "Check - error string (content delete)");
		user = new KatelloUser(cli_worker, user_publish, "root@localhost", KatelloUser.DEFAULT_USER_PASS, false);
		content.runAs(user);
		exec_result = content.delete();
		Assert.assertTrue(exec_result.getExitCode()==147, "Check = error code (delete content definition)");
		Assert.assertTrue(getOutput(exec_result).equals(String.format(KatelloContentDefinition.ERR_DELETE_DENIED, this.user_publish)), "Check - error string (content delete)");
		
		user = new KatelloUser(cli_worker, user_modify, "root@localhost", KatelloUser.DEFAULT_USER_PASS, false);
		content.runAs(user);
		exec_result = content.delete();
		Assert.assertTrue(exec_result.getExitCode()==147, "Check = error code (delete content definition)");
		Assert.assertTrue(getOutput(exec_result).equals(String.format(KatelloContentDefinition.ERR_DELETE_DENIED, this.user_modify)), "Check - error string (content delete)");
	}

	@Test(description="check permissions to delete content definition",dependsOnMethods={"init"})
	public void test_DeleteAccess() {
		KatelloUser user = new KatelloUser(cli_worker, user_delete, "root@localhost", KatelloUser.DEFAULT_USER_PASS, false);
		KatelloContentDefinition content = new KatelloContentDefinition(cli_worker, content_name3, "description", base_org_name, content_name3);
		content.runAs(user);
		exec_result = content.delete();
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - return code (delete content definition)");
		Assert.assertTrue(getOutput(exec_result).equals(String.format(KatelloContentDefinition.OUT_DELETE_DEFINITION, content_name3)), "Check - output message");
	}

	@Test(description="publish access", dependsOnMethods={"init"})
	public void test_PublishAccess() {
		KatelloUser user = new KatelloUser(cli_worker, user_publish, "root@localhost", KatelloUser.DEFAULT_USER_PASS, false);
		KatelloContentDefinition content = new KatelloContentDefinition(cli_worker, content_publish1, "description", base_org_name, content_publish1);
		content.runAs(user);
		exec_result = content.publish("view_name1", "view_name1", "view description");
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (publish content)");
		Assert.assertContainsMatch(getOutput(exec_result), "published successfully", null, "Check - output message (publish content)");
	}

	@Test(description="publish no access", dependsOnMethods={"init"})
	public void test_PublishNoAccess() {
		KatelloUser user = new KatelloUser(cli_worker, user_read, "root@localhost", KatelloUser.DEFAULT_USER_PASS, false);
		KatelloContentDefinition content = new KatelloContentDefinition(cli_worker, content_publish2, "description", base_org_name, content_publish2);
		content.runAs(user);
		exec_result = content.publish("view_name2", "view_name2", "view description");
		Assert.assertTrue(exec_result.getExitCode()==147, "Check - error code (publish content)");
		Assert.assertTrue(getOutput(exec_result).equals(String.format(KatelloContentDefinition.ERR_PUBLISH_DENIED, user_read)), "Check - error string (content create)");
	}

	@Test(description="read access to content definition", dependsOnMethods={"init"})
	public void test_ReadAccess() {
		KatelloUser user = new KatelloUser(cli_worker, user_read, "root@localhost", KatelloUser.DEFAULT_USER_PASS, false);
		KatelloContentDefinition content = new KatelloContentDefinition(cli_worker, content_publish1, "description", base_org_name, content_publish1);
		content.runAs(user);
		exec_result = content.info();
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - return code (content info)");
		exec_result = content.list();
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - return code (content list)");
	}

	@Test(description="no access to content definition", dependsOnMethods={"init"})
	public void test_ReadNoAccess() {
		String uid = KatelloUtils.getUniqueID();
		String user_nobody = "nobody" + uid;
		KatelloUser user = new KatelloUser(cli_worker, user_nobody, "root@localhost", KatelloUser.DEFAULT_USER_PASS, false);
		exec_result = user.cli_create();
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - return code (acreted user)");
		KatelloContentDefinition content = new KatelloContentDefinition(cli_worker, content_publish1, "description", base_org_name, content_publish1);
		content.runAs(user);
		exec_result = content.info();
		Assert.assertTrue(exec_result.getExitCode()==147, "Check - return code (content info)");
		exec_result = content.list();
		Assert.assertTrue(exec_result.getExitCode()==147, "Check - return code (content list)");
	}

	@Test(description="modify access to content view definition", dependsOnMethods={"init"})
	public void test_modifyAccess() {
		KatelloUser user = new KatelloUser(cli_worker, user_modify, "root@localhost", KatelloUser.DEFAULT_USER_PASS, false);
		KatelloContentDefinition content = new KatelloContentDefinition(cli_worker, content_name4, "description", base_org_name, content_name4);
		content.runAs(user);
		exec_result = content.update("new description");
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (update content)");
		Assert.assertTrue(getOutput(exec_result).equals(String.format(KatelloContentDefinition.OUT_UPDATE, content_name4)), "Check output");
	}

	@Test(description="no modify access to content view definition", dependsOnMethods={"init"})
	public void test_modifyNoAccess() {
		KatelloUser user = new KatelloUser(cli_worker, user_read, "root@localhost", KatelloUser.DEFAULT_USER_PASS, false);
		KatelloContentDefinition content = new KatelloContentDefinition(cli_worker, content_name4, "description", base_org_name, content_name4);
		content.runAs(user);
		exec_result = content.update("new description II");
		Assert.assertTrue(exec_result.getExitCode()==147, "Check - exit code (update content)");
		Assert.assertTrue(getOutput(exec_result).equals(String.format(KatelloContentDefinition.ERR_UPDATE, user_read)), "Check output");
	}

	@Test(description="test read permission to content view", dependsOnMethods={"init"})
	public void test_ReadPublishedAccess() {
		KatelloUser user = new KatelloUser(cli_worker, user_rpub, KatelloUser.DEFAULT_USER_EMAIL, KatelloUser.DEFAULT_USER_PASS, false);
		KatelloContentView content = new KatelloContentView(cli_worker, view_name1, "description", base_org_name, view_name1);
		content.runAs(user);
		exec_result = content.view_list();
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (list views)");
		Assert.assertTrue(getOutput(exec_result).contains(view_name1), "Check - output");
		Assert.assertFalse(getOutput(exec_result).contains(view_name2), "Check - output");
		exec_result = content.view_info();
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (list views)");
	}

	@Test(description="test read permission to content view", dependsOnMethods={"init"})
	public void test_ReadPublishedNoAccess() {
		KatelloUser user = new KatelloUser(cli_worker, user_read, KatelloUser.DEFAULT_USER_EMAIL, KatelloUser.DEFAULT_USER_PASS, false);
		KatelloContentView content = new KatelloContentView(cli_worker, view_name1, "description", base_org_name, view_name1);
		content.runAs(user);
		exec_result = content.view_list();
		Assert.assertTrue(exec_result.getExitCode()==147, "Check - exit code (list views)");
		Assert.assertTrue(getOutput(exec_result).equals(String.format(KatelloContentView.ERR_VIEW_READ, this.user_read)), "Check - error string (view read)");
		exec_result = content.view_info();
		Assert.assertTrue(exec_result.getExitCode()==147, "Check - exit code (list views)");
		Assert.assertTrue(getOutput(exec_result).equals(String.format(KatelloContentView.ERR_VIEW_READ, this.user_read)), "Check - error string (view read)");
	}

	@Test(description="access to promote content views", dependsOnMethods={"init"})
	public void test_PromoteAccess() {
		KatelloUser user = new KatelloUser(cli_worker, user_prom, KatelloUser.DEFAULT_USER_EMAIL, KatelloUser.DEFAULT_USER_PASS, false);
		KatelloContentView content = new KatelloContentView(cli_worker, view_pub1, "description", base_org_name, view_pub1);
		content.runAs(user);
		exec_result = content.promote_view(base_test_env_name);
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code (promote view)");
		Assert.assertTrue(getOutput(exec_result).contains(String.format(KatelloContentView.OUT_PROMOTE, view_pub1, base_test_env_name)), "Check - output message");
	}

	@Test(description="promote no access", dependsOnMethods={"init"})
	public void test_PromoteNoAccess() {
		KatelloUser user = new KatelloUser(cli_worker, user_prom, KatelloUser.DEFAULT_USER_EMAIL, KatelloUser.DEFAULT_USER_PASS, false);
		KatelloContentView content = new KatelloContentView(cli_worker, view_pub2, "description", base_org_name, view_pub2);
		content.runAs(user);
		exec_result = content.promote_view(base_test_env_name);
		Assert.assertTrue(exec_result.getExitCode()==147, "Check - exit code (promote view)");
		Assert.assertTrue(getOutput(exec_result).equals(String.format(KatelloContentView.ERR_PROMOTE_DENIED, user_prom)), "Check - output message");		
	}

	@Test(description="subscribe access to content view", dependsOnMethods={"init"})
	public void test_SubscribeAccess() {
		KatelloUser user = new KatelloUser(cli_worker, subscr_user, KatelloUser.DEFAULT_USER_EMAIL, KatelloUser.DEFAULT_USER_PASS, false);
		KatelloSystem sys = new KatelloSystem(this.cli_worker, sys_name, base_org_name, base_test_env_name);
		sys.runAs(user);
		exec_result = sys.update_content_view(subscr_view1);
		Assert.assertTrue(exec_result.getExitCode()==0, "Check - exit code");
		Assert.assertTrue(getOutput(exec_result).equals(String.format(KatelloSystem.OUT_UPDATE, sys_name)), "Check - output message");
	}

	@Test(description="no subscribe access to content view", dependsOnMethods={"init"})
	public void test_SubscribeNoAccess() {
		KatelloUser user = new KatelloUser(cli_worker, subscr_user, KatelloUser.DEFAULT_USER_EMAIL, KatelloUser.DEFAULT_USER_PASS, false);
		KatelloSystem sys = new KatelloSystem(this.cli_worker, sys_name, base_org_name, base_test_env_name);
		sys.runAs(user);
		exec_result = sys.update_content_view(subscr_view2);
		Assert.assertTrue(exec_result.getExitCode()==147, "Check - exit code");
		Assert.assertTrue(getOutput(exec_result).equals(String.format(KatelloSystem.ERR_UPDATE, subscr_user)), "Check - output message");
	}
}
