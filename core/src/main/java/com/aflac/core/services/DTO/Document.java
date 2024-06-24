package com.aflac.core.services.DTO;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Document{
    @JsonProperty("ObjectStore") 
    public String objectStore;
    @JsonProperty("DocumentClass") 
    public String documentClass;
    @JsonProperty("PrimaryDocClass") 
    public String primaryDocClass;
    @JsonProperty("SecondaryDocClass") 
    public String secondaryDocClass;
    @JsonProperty("ContentType") 
    public String contentType;
    @JsonProperty("MimeType") 
    public String mimeType;
    @JsonProperty("RecCatID") 
    public String recCatID;
    @JsonProperty("SubRecCatID") 
    public String subRecCatID;
    @JsonProperty("BrochureID") 
    public String brochureID;
    @JsonProperty("ClientManager") 
    public String clientManager;
    @JsonProperty("CoverageEffDate") 
    public String coverageEffDate;
    @JsonProperty("DocumentLanguage") 
    public String documentLanguage;
    @JsonProperty("EnrollmentEndDate") 
    public String enrollmentEndDate;
    @JsonProperty("EnrollmentStartDate") 
    public String enrollmentStartDate;
    @JsonProperty("EPSProposalID") 
    public String ePSProposalID;
    @JsonProperty("GroupPlanID") 
    public String groupPlanID;
    @JsonProperty("ImplementationManager") 
    public String implementationManager;
    @JsonProperty("LOB") 
    public String lOB;
    @JsonProperty("OnboardingCaseID") 
    public String onboardingCaseID;
    @JsonProperty("PartnerPlatformManager") 
    public String partnerPlatformManager;
    @JsonProperty("PlatformName") 
    public String platformName;
    @JsonProperty("ProductSeries") 
    public String productSeries;
    @JsonProperty("SignatureStatus") 
    public String signatureStatus;
    @JsonProperty("SitusState") 
    public String situsState;
    @JsonProperty("State") 
    public String state;
    @JsonProperty("WebOrderingFormIDs") 
    public String webOrderingFormIDs;
    @JsonProperty("Properties") 
    public Properties properties;
}






