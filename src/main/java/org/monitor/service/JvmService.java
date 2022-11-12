package org.monitor.service;

import com.sun.tools.attach.*;
import org.monitor.entity.JVM;
import org.springframework.stereotype.Service;
import sun.management.ConnectorAddressLink;

import javax.management.remote.JMXConnector;
import javax.management.remote.JMXConnectorFactory;
import javax.management.remote.JMXServiceURL;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;



@Service
public class JvmService {

    public static volatile JMXConnector jmxConnector = null;

    public static volatile Integer connectPid = null;

    public List<JVM> getJvm() {
        List<VirtualMachineDescriptor> machineDescriptors = VirtualMachine.list();
        List<JVM> jvms = new ArrayList<>(machineDescriptors.size());
        machineDescriptors.forEach(machineDescriptor -> {
            JVM jvm = new JVM();
            jvm.setPid(Integer.valueOf(machineDescriptor.id()));
            jvm.setName(machineDescriptor.displayName());

            jvms.add(jvm);
        });

        return jvms;
    }


    public void attach(Integer pid) throws IOException, AttachNotSupportedException {
        if (jmxConnector != null) {
            throw new RuntimeException("连接已建立");
        }

        synchronized (JvmService.class) {
            String address = ConnectorAddressLink.importFrom(pid);
            JMXServiceURL serviceURL = null;
            if (address == null) {
                VirtualMachine virtualMachine = VirtualMachine.attach(Integer.toString(pid));
                //加载Agent
                String javaHome = virtualMachine.getSystemProperties().getProperty("java.home");
                String agentPath = javaHome + File.separator + "jre" + File.separator + "lib" + File.separator + "management-agent.jar";
                File file = new File(agentPath);
                if (!file.exists()) {
                    agentPath = javaHome + File.separator + "lib" + File.separator + "management-agent.jar";
                    file = new File(agentPath);
                    if (!file.exists()) {
                        throw new IOException("Management agent not found");
                    }
                }
                agentPath = file.getCanonicalPath();
                try {
                    virtualMachine.loadAgent(agentPath, "com.sun.management.jmxremote");
                } catch (AgentLoadException | AgentInitializationException e) {
                    throw new IOException(e);
                }

                Properties properties = virtualMachine.getAgentProperties();

                address = (String) properties.get("com.sun.management.jmxremote.localConnectorAddress");
                if (address != null) {
                    serviceURL = new JMXServiceURL(address);
                }
            } else {
                serviceURL = new JMXServiceURL(address);
            }

            if (jmxConnector != null) {
                throw new RuntimeException("连接已建立");
            }

            connectPid = pid;
            jmxConnector = JMXConnectorFactory.connect(serviceURL, null);
        }

    }
}
