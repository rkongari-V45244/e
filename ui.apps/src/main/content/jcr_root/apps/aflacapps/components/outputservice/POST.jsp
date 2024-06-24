<%--
	 use output service to merge data into xdp or pdf
--%>
    <%@include file="/libs/foundation/global.jsp"%>
	<%@page session="false" %>
<%

		System.out.println("I am in output service POST.jsp");
		System.out.println("The parameters I got in the request were");

		String RESOURCEPATH = "/content/dam/formsanddocuments/aflacapps/group-master-application-pdf-templates/";

		if(request.getParameter("preview")==null)
        {
			com.aflac.core.servlets.Utility.saveXML(sling.getRequest());
        }

		byte[] xmlIS = request.getParameter("formData").getBytes();


		com.adobe.aemfd.docmanager.Document xdpDocument = new com.adobe.aemfd.docmanager.Document(
				RESOURCEPATH + "C02204.xdp");
		com.adobe.aemfd.docmanager.Document xmlDocument = new com.adobe.aemfd.docmanager.Document(xmlIS);
		com.adobe.fd.output.api.OutputService outputService = sling
				.getService(com.adobe.fd.output.api.OutputService.class);

		if (outputService == null) {
			System.out.println("The output service is  null.....");
		} else {
			System.out.println("The output service is  not null.....");

		}

		com.adobe.fd.output.api.PDFOutputOptions pdfOptions = new com.adobe.fd.output.api.PDFOutputOptions();
		pdfOptions.setAcrobatVersion(com.adobe.fd.output.api.AcrobatVersion.Acrobat_11);
		com.adobe.aemfd.docmanager.Document generatedDocument = outputService.generatePDFOutput(xdpDocument,
				xmlDocument, pdfOptions);

		if(request.getParameter("preview") == null)
        {
			String filePath = com.aflac.core.servlets.Utility.savePDF(sling.getRequest(),generatedDocument.getInputStream());
			out.println(filePath);
		} else {
            sling.getResponse().setContentType("application/pdf");
			sling.getResponse().addHeader("Content-Disposition", "attachment; filename=output.pdf");
			java.io.OutputStream responseOutputStream = sling.getResponse().getOutputStream();			
			org.apache.commons.io.IOUtils.copy(generatedDocument.getInputStream(),responseOutputStream);
			responseOutputStream.flush();
			responseOutputStream.close();
		}


/*
	javax.servlet.http.Part xdpPart = request.getPart("template");
	System.out.println("Got xdp_or_pdf_file");
	javax.servlet.http.Part xmlDataPart = request.getPart("data");
	System.out.println("Got xml file");
	String filePath = request.getParameter("saveLocation");
	java.io.InputStream xdpIS = xdpPart.getInputStream();
	System.out.println("Got xdp input stream");
	java.io.InputStream xmlIS = xmlDataPart.getInputStream();
	com.adobe.aemfd.docmanager.Document xdpDocument = new com.adobe.aemfd.docmanager.Document(xdpIS);
	com.adobe.aemfd.docmanager.Document xmlDocument = new com.adobe.aemfd.docmanager.Document(xmlIS);
	com.adobe.fd.output.api.OutputService outputService = sling.getService(com.adobe.fd.output.api.OutputService.class);

	if(outputService ==null)
		{
    		System.out.println("The output service is  null.....");
		}
		else
        {
            System.out.println("The output service is  not null.....");

        }
	com.adobe.fd.output.api.PDFOutputOptions pdfOptions = new com.adobe.fd.output.api.PDFOutputOptions();
	pdfOptions.setAcrobatVersion(com.adobe.fd.output.api.AcrobatVersion.Acrobat_11);
	com.adobe.aemfd.docmanager.Document generatedDocument = outputService.generatePDFOutput(xdpDocument,xmlDocument,pdfOptions);
	generatedDocument.copyToFile(new java.io.File(filePath));
	out.println("Document genreated and saved to " +filePath);
    */
%>