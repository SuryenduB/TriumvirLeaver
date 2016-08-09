package com.triumvir.leaver;

import java.util.ArrayList;
import java.util.List;

import sailpoint.api.SailPointContext;
import sailpoint.object.Entitlement;
import sailpoint.object.Identity;
import sailpoint.object.Link;
import sailpoint.object.ProvisioningPlan;
import sailpoint.object.ProvisioningPlan.AccountRequest;
import sailpoint.object.ProvisioningPlan.AttributeRequest;
import sailpoint.tools.GeneralException;

public class EntitlementOperations 
{
	public ProvisioningPlan getDeleteEntitlementProvisioninPlan(Identity identity, SailPointContext context, List <String> exclusionList) throws GeneralException 
	{
		ProvisioningPlan plan = new ProvisioningPlan();
		plan.setIdentity(identity);

		List <AccountRequest> listAccountRequest = new ArrayList<AccountRequest>();
		
		List<Link> accounts = identity.getLinks();
		// Get all the account entitlement's.
		for(Link account : accounts)
		{
			List<Entitlement> entitlements = account.getEntitlements(null, null);
			for(Entitlement entitlement : entitlements)
			{
				AccountRequest accRequest = new AccountRequest();
				accRequest.setApplication(account.getApplicationName());
				accRequest.setNativeIdentity(account.getNativeIdentity());
				accRequest.setOperation(AccountRequest.Operation.Modify);
				
				AttributeRequest attributeRequest = new AttributeRequest();
				attributeRequest.setName(entitlement.getAttributeName());
				attributeRequest.setValue(entitlement.getAttributeValue());
				attributeRequest.setOperation(ProvisioningPlan.Operation.Remove);
				accRequest.add(attributeRequest);
				listAccountRequest.add(accRequest);
			}
		}
		
		plan.setAccountRequests(listAccountRequest);
		System.out.println(plan.toXml());
		return plan;
	}
	
}
