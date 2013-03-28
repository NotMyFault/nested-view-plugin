package hudson.plugins.nested_view;

import com.google.common.collect.Lists;
import hudson.DescriptorExtensionList;
import hudson.model.Descriptor;
import hudson.util.DescribableList;
import hudson.views.ListViewColumn;
import hudson.views.ListViewColumnDescriptor;
import hudson.views.StatusColumn;
import hudson.views.WeatherColumn;
import jenkins.model.Jenkins;
import net.sf.json.JSONObject;
import org.kohsuke.stapler.StaplerRequest;

import java.io.IOException;
import java.util.List;

/**
 * @author sbashkyrtsev
 */
public class AvailableColumns {
    private DescribableList<ListViewColumn, Descriptor<ListViewColumn>> columns;

    public void setColumns(DescribableList<ListViewColumn, Descriptor<ListViewColumn>> columns) {
        this.columns = columns;
    }

    public DescribableList<ListViewColumn, Descriptor<ListViewColumn>> getColumns() {
        return columns;
    }

    public void updateFromForm(StaplerRequest req, JSONObject formData, String key) throws IOException, Descriptor.FormException {
        columns.rebuildHetero(req, formData, nestedViewColumns(), key);
    }

    public boolean isShowStatusColumn() {
        return containsColumnWithDescriptor(StatusColumn.DescriptorImpl.class);
    }

    public boolean isShowWeatherColumn() {
        return containsColumnWithDescriptor(WeatherColumn.DescriptorImpl.class);
    }

    private boolean containsColumnWithDescriptor(Class<? extends ListViewColumnDescriptor> descriptorClass) {
        for (ListViewColumn column : columns) {
            if (column.getDescriptor().getClass() == descriptorClass) {
                return true;
            }
        }
        return false;
    }

    public static List<Descriptor<ListViewColumn>> nestedViewColumns() {
        return extractOptionalColumns(allViewColumns());
    }

    private static DescriptorExtensionList<ListViewColumn, Descriptor<ListViewColumn>> allViewColumns() {
        return Jenkins.getInstance().getDescriptorList(ListViewColumn.class);
    }

    private static List<Descriptor<ListViewColumn>> extractOptionalColumns(
            DescriptorExtensionList<ListViewColumn, Descriptor<ListViewColumn>> extensionList) {
        return Lists.<Descriptor<ListViewColumn>>newArrayList(
                extensionList.get(WeatherColumn.DescriptorImpl.class),
                extensionList.get(StatusColumn.DescriptorImpl.class));
    }
}
